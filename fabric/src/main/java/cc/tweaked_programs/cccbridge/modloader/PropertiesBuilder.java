package cc.tweaked_programs.cccbridge.modloader;

import cc.tweaked_programs.cccbridge.Registries;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class PropertiesBuilder {
    protected static final CreativeModeTab CCCGROUP = FabricItemGroupBuilder.build(new ResourceLocation(CCCBridge.MOD_ID, "cccbridge_group"), () -> new ItemStack((Item)Registries.ANIMATRONIC_BLOCK_ITEM.get()));
    public static final FabricItemSettings PROPERTIES = new FabricItemSettings().group(CCCGROUP).maxCount(64);
}