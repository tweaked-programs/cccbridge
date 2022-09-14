package cc.tweaked_programs.cccbridge;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class BlockRegister {
    private static final Map<String, BlockEntityType> blockEntitys = new HashMap();
    private static final Map<String, Block> blocks = new HashMap();
    private static final Map<String, Item> items = new HashMap();

    private static ItemGroup group = ItemGroup.REDSTONE;


    public static <T extends BlockEntity> void registerBlockEntity(String id, FabricBlockEntityTypeBuilder.Factory<? extends T> factory, Block block) {
        blockEntitys.put(id,
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new Identifier(CCCBridge.MOD_ID, id + "_entity"), FabricBlockEntityTypeBuilder.create(factory, block).build(null))
        );
        blocks.put(id, block);

        Item item = new BlockItem(block, new FabricItemSettings().group(group));
        items.put(id, item);
        Registry.register(Registry.BLOCK, new Identifier(CCCBridge.MOD_ID, id), block);
        Registry.register(Registry.ITEM, new Identifier(CCCBridge.MOD_ID, id), item);
    }

    public static BlockEntityType getBlockEntityType(String id) {
        return blockEntitys.get(id);
    }

    public static Block getBlock(String id) {
        return blocks.get(id);
    }

}
