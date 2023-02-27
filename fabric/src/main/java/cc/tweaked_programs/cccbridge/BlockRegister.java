package cc.tweaked_programs.cccbridge;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

public class BlockRegister {
    private static final Map<String, BlockEntityType> blockEntitys = new HashMap<>();
    private static final Map<String, Block> blocks = new HashMap<>();
    private static final Map<String, Item> items = new HashMap<>();

    private static final CreativeModeTab group = CreativeModeTab.TAB_REDSTONE;


    public static VoxelShape newBox(float p1, float p2, float p3, float p4, float p5, float p6) {
        return Block.box(p1*16,p2*16,p3*16,p4*16,p5*16,p6*16);
    }

    public static <T extends BlockEntity> void registerBlockEntity(String id, FabricBlockEntityTypeBuilder.Factory<? extends T> factory, Block block) {
        blockEntitys.put(id,
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(CCCBridge.MOD_ID, id + "_entity"), FabricBlockEntityTypeBuilder.create(factory, block).build(null))
        );
        blocks.put(id, block);

        Item item = new BlockItem(block, new FabricItemSettings().group(group));
        items.put(id, item);
        Registry.register(Registry.BLOCK, new ResourceLocation(CCCBridge.MOD_ID, id), block);
        Registry.register(Registry.ITEM, new ResourceLocation(CCCBridge.MOD_ID, id), item);
    }

    public static BlockEntityType<?> getBlockEntityType(String id) {
        return blockEntitys.get(id);
    }

    @Nullable
    public static Block getBlock(String id) {
        return blocks.get(id);
    }
}
