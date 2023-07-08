package cc.tweaked_programs.cccbridge;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
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

public class CCCRegister {
    private static final Map<String, BlockEntityType<? extends BlockEntity>> blockEntities = new HashMap<>();
    private static final Map<String, Block> blocks = new HashMap<>();
    private static final Map<String, Item> items = new HashMap<>();
    private static final Map<String, EntityType<? extends Entity>> entities = new HashMap<>();

    private static final CreativeModeTab group = CreativeModeTab.TAB_REDSTONE;


    public static VoxelShape newBox(float p1, float p2, float p3, float p4, float p5, float p6) {
        return Block.box(p1*16,p2*16,p3*16,p4*16,p5*16,p6*16);
    }

    public static <T extends BlockEntity> void registerBlockEntity(String id, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
        blockEntities.put(id,
                Registry.register(Registry.BLOCK_ENTITY_TYPE, new ResourceLocation(CCCBridge.MOD_ID, id + "_entity"), FabricBlockEntityTypeBuilder.create(factory, block).build(null))
        );
        blocks.put(id, block);

        Item item = new BlockItem(block, new FabricItemSettings().group(group));
        items.put(id, item);
        Registry.register(Registry.BLOCK, new ResourceLocation(CCCBridge.MOD_ID, id), block);
        Registry.register(Registry.ITEM, new ResourceLocation(CCCBridge.MOD_ID, id), item);
    }

    public static <T extends Entity, R extends EntityRendererProvider<T>> void registerEntity(String id, EntityType<T> entitytype, R renderer, ModelLayerLocation layerLocation, EntityModelLayerRegistry.TexturedModelDataProvider createBodyLayer) {
        EntityType<T> registeredEntityType = Registry.register(Registry.ENTITY_TYPE, new ResourceLocation(CCCBridge.MOD_ID, id), entitytype);

        EntityRendererRegistry.register(registeredEntityType, renderer);
        EntityModelLayerRegistry.registerModelLayer(layerLocation, createBodyLayer);

        entities.put(id, registeredEntityType);
    }

    public static BlockEntityType<? extends BlockEntity> getBlockEntityType(String id) {
        return blockEntities.get(id);
    }

    public static EntityType<? extends Entity> getEntityType(String id) {
        return entities.get(id);
    }

    @Nullable
    public static Block getBlock(String id) {
        return blocks.get(id);
    }

    public static ResourceLocation getEntityTypeId(String id) {
        return new ResourceLocation(CCCBridge.MOD_ID, id);
    }
}
