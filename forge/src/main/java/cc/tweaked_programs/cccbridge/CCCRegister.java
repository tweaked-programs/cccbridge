package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.*;
import cc.tweaked_programs.cccbridge.blockEntity.*;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicEntity;
import com.mojang.datafixers.types.Type;
import net.minecraft.Util;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.Motive;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CCCRegister {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CCCBridge.MOD_ID);
    private static final DeferredRegister<Motive> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_TYPES, CCCBridge.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CCCBridge.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, CCCBridge.MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCCBridge.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITIES, CCCBridge.MOD_ID);

    public static final RegistryObject<Block> SOURCE_BLOCK = BLOCKS.register("source_block", SourceBlock::new);
    public static final RegistryObject<Block> TARGET_BLOCK = BLOCKS.register("target_block", TargetBlock::new);
    public static final RegistryObject<Block> REDROUTER_BLOCK = BLOCKS.register("redrouter_block", RedRouterBlock::new);
    public static final RegistryObject<Block> SCROLLER_BLOCK = BLOCKS.register("scroller_block", ScrollerBlock::new);
    public static final RegistryObject<Block> ANIMATRONIC_BLOCK = BLOCKS.register("animatronic_block", AnimatronicBlock::new);

    private static Type<?> getType(String key) {
        return Util.fetchChoiceType(References.BLOCK_ENTITY, key);
    }

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE_BLOCK_ENTITY = BLOCK_ENTITIES.register("source_block_entity", () -> BlockEntityType.Builder.of(SourceBlockEntity::new, SOURCE_BLOCK.get()).build(getType("source_block")));
    public static final RegistryObject<BlockEntityType<TargetBlockEntity>> TARGET_BLOCK_ENTITY = BLOCK_ENTITIES.register("target_block_entity", () -> BlockEntityType.Builder.of(TargetBlockEntity::new, TARGET_BLOCK.get()).build(getType("target_block")));
    public static final RegistryObject<BlockEntityType<RedRouterBlockEntity>> REDROUTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("redrouter_block_entity", () -> BlockEntityType.Builder.of(RedRouterBlockEntity::new, REDROUTER_BLOCK.get()).build(getType("redrouter_block")));
    public static final RegistryObject<BlockEntityType<ScrollerBlockEntity>> SCROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("scroller_block_entity", () -> BlockEntityType.Builder.of(ScrollerBlockEntity::new, SCROLLER_BLOCK.get()).build(getType("scroller_block")));
    public static final RegistryObject<BlockEntityType<AnimatronicBlockEntity>> ANIMATRONIC_BLOCK_ENTITY = BLOCK_ENTITIES.register("animatronic_block_entity", () -> BlockEntityType.Builder.of(AnimatronicBlockEntity::new, ANIMATRONIC_BLOCK.get()).build(getType("animatronic_block")));

    public static final RegistryObject<EntityType<AnimatronicEntity>> ANIMATRONIC_ENTITY = ENTITIES.register("animatronic", () -> EntityType.Builder.of(AnimatronicEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build(new ResourceLocation(CCCBridge.MOD_ID, "animatronic").toString()));

    public static void register() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ITEMS.register("source_block", () -> new BlockItem(SOURCE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("target_block", () -> new BlockItem(TARGET_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("redrouter_block", () -> new BlockItem(REDROUTER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("scroller_block", () -> new BlockItem(SCROLLER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("animatronic_block", () -> new BlockItem(ANIMATRONIC_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        PAINTINGS.register("funny_redrouters", () -> new Motive(32,16));
        PAINTINGS.register(FMLJavaModLoadingContext.get().getModEventBus());

        CCCSoundEvents.init(SOUNDS);
    }
}
