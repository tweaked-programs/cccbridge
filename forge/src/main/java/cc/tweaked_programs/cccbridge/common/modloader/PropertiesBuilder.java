package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PropertiesBuilder {
    public static final CreativeModeTab.Builder CCCGROUP = new CreativeModeTab.Builder(CreativeModeTab.Row.BOTTOM, -1)
            .icon(() -> new ItemStack(CCCRegistries.ANIMATRONIC_BLOCK_ITEM.get()))
            .title(Component.translatable("itemGroup.cccbridge.cccbridge_group"))
            .displayItems((parameters, output) -> CCCRegistries.ITEMS.getEntries().stream().map(entry -> entry.get()).forEachOrdered(output::accept));
    public static final Item.Properties PROPERTIES = new Item.Properties().stacksTo(64);
}