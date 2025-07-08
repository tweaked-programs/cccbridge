package dev.kleinbox.cccbridge.common.minecraft;

import dev.kleinbox.cccbridge.common.assistance.CharsetManipulator;
import net.createmod.catnip.lang.LangNumberFormat;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;

import static dev.kleinbox.cccbridge.common.modloader.PropertiesBuilder.PROPERTIES;

public class TweakedBlockItem extends BlockItem {
    public static final int TOOLTIP_WIDTH = 25;

    private final double version;

    public TweakedBlockItem(Block block, @Nullable double version) {
        super(block, PROPERTIES);
        this.version = version;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void appendHoverText(@NotNull ItemStack stack, Item.@NotNull TooltipContext context, @NotNull List<Component> tooltipComponents, @NotNull TooltipFlag tooltipFlag) {
        String tip = Component.translatable("cccbridge.tooltip.holdForDescription").getString();
        boolean holdsShift = Screen.hasShiftDown();

        // Begin
        tooltipComponents.add(Component.literal(MessageFormat.format(tip, (holdsShift ? "§f" : "§7"))));

        // Description
        if (holdsShift) {
            tooltipComponents.add(Component.empty());
            String[] lines = Component.translatable(super.getDescriptionId() + ".description").getString().split("\n");

            for (String line : lines) {
                if (line.length() > TOOLTIP_WIDTH)
                    for (String sub : CharsetManipulator.wrap(line, TOOLTIP_WIDTH))
                        tooltipComponents.add(Component.literal(sub));
                else
                    tooltipComponents.add(Component.literal(line));
            }
        }

        // Advanced details
        if (tooltipFlag.isAdvanced()) {
            if (holdsShift)
                tooltipComponents.add(Component.empty());

            if (version > 0) {
                String peripheral = Component.translatable("cccbridge.tooltip.peripheral").getString();
                tooltipComponents.add(Component.literal(MessageFormat.format(peripheral, LangNumberFormat.format(version))));
            } else
                tooltipComponents.add(Component.translatable("cccbridge.tooltip.no_peripheral"));
        }
    }
}
