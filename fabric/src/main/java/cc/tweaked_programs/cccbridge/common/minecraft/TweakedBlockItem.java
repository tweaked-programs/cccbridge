package cc.tweaked_programs.cccbridge.common.minecraft;

import cc.tweaked_programs.cccbridge.common.assistance.CharsetManipulator;
import com.simibubi.create.foundation.utility.LangNumberFormat;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.text.MessageFormat;
import java.util.List;

import static cc.tweaked_programs.cccbridge.common.modloader.PropertiesBuilder.PROPERTIES;

public class TweakedBlockItem extends BlockItem {
    public static final int TOOLTIP_WIDTH = 25;

    private final double version;

    public TweakedBlockItem(Block block, @Nullable double version) {
        super(block, PROPERTIES);
        this.version = version;
    }

    @Environment(EnvType.CLIENT)
    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level level, List<Component> tooltip, TooltipFlag flag) {
        String tip = Component.translatable("cccbridge.tooltip.holdForDescription").getString();
        boolean holdsShift = Screen.hasShiftDown();

        // Begin
        tooltip.add(Component.literal(MessageFormat.format(tip, (holdsShift ? "§f" : "§7"))));

        // Description
        if (holdsShift) {
            tooltip.add(Component.empty());
            String[] lines = Component.translatable(super.getDescriptionId() + ".description").getString().split("\n");

            for (String line : lines) {
                if (line.length() > TOOLTIP_WIDTH)
                    for (String sub : CharsetManipulator.wrap(line, TOOLTIP_WIDTH))
                        tooltip.add(Component.literal(sub));
                else
                    tooltip.add(Component.literal(line));
            }
        }

        // Advanced details
        if (flag.isAdvanced()) {
            if (holdsShift)
                tooltip.add(Component.empty());

            if (version > 0) {
                String peripheral = Component.translatable("cccbridge.tooltip.peripheral").getString();
                tooltip.add(Component.literal(MessageFormat.format(peripheral, LangNumberFormat.format(version))));
            } else
                tooltip.add(Component.translatable("cccbridge.tooltip.no_peripheral"));
        }
    }
}
