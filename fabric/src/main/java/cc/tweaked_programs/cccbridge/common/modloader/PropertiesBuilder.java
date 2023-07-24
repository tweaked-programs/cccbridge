package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PropertiesBuilder {
    public static final CreativeModeTab CCCGROUP = FabricItemGroup.builder()
            .icon(() -> new ItemStack((Item) CCCRegistries.ANIMATRONIC_BLOCK_ITEM.get()))
            .title(Component.translatable("itemGroup.cccbridge.cccbridge_group"))
            .build();
    public static final FabricItemSettings PROPERTIES = new FabricItemSettings().maxCount(64);
}