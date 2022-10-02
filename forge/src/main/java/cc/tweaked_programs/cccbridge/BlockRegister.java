package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.RedRouterBlock;
import cc.tweaked_programs.cccbridge.block.SourceBlock;
import cc.tweaked_programs.cccbridge.block.TargetBlock;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BlockRegister {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CCCBridge.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CCCBridge.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCCBridge.MOD_ID);

    public static final RegistryObject<Block> SOURCE_BLOCK = BLOCKS.register("source_block", SourceBlock::new);
    public static final RegistryObject<Block> TARGET_BLOCK = BLOCKS.register("target_block", TargetBlock::new);
    public static final RegistryObject<Block> REDROUTER_BLOCK = BLOCKS.register("redrouter_block", RedRouterBlock::new);

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE_BLOCK_ENTITY = BLOCK_ENTITIES.register("source_block_entity", () -> BlockEntityType.Builder.of(SourceBlockEntity::new, SOURCE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<TargetBlockEntity>> TARGET_BLOCK_ENTITY = BLOCK_ENTITIES.register("target_block_entity", () -> BlockEntityType.Builder.of(TargetBlockEntity::new, TARGET_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<RedRouterBlockEntity>> REDROUTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("redrouter_block_entity", () -> BlockEntityType.Builder.of(RedRouterBlockEntity::new, REDROUTER_BLOCK.get()).build(null));

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ITEMS.register("source_block", () -> new BlockItem(SOURCE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("target_block", () -> new BlockItem(TARGET_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("redrouter_block", () -> new BlockItem(REDROUTER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
}
