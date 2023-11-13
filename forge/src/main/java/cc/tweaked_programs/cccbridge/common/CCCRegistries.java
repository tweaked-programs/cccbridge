package cc.tweaked_programs.cccbridge.common;

import cc.tweaked_programs.cccbridge.common.computercraft.TweakedPeripheralProvider;
import cc.tweaked_programs.cccbridge.common.computercraft.peripherals.*;
import cc.tweaked_programs.cccbridge.common.create.behaviour.AnimatronicInteractionBehaviour;
import cc.tweaked_programs.cccbridge.common.create.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.common.create.display.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.common.minecraft.TweakedBlockItem;
import cc.tweaked_programs.cccbridge.common.minecraft.block.*;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.*;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import cc.tweaked_programs.cccbridge.common.modloader.PropertiesBuilder;
import com.simibubi.create.AllInteractionBehaviours;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import static cc.tweaked_programs.cccbridge.common.minecraft.block.RedRouterBlock.REDROUTER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.ScrollerBlock.SCROLLER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.SourceBlock.SOURCE_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.TargetBlock.TARGET_BLOCK_PROPERTIES;

@SuppressWarnings({"DataFlowIssue", "unused"})
public class CCCRegistries {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, CCCBridge.MOD_ID);
    private static final DeferredRegister<PaintingVariant> PAINTINGS = DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, CCCBridge.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, CCCBridge.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, CCCBridge.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, CCCBridge.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CCCBridge.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCCBridge.MOD_ID);

    public static final RegistryObject<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("cccbridge_group", PropertiesBuilder.CCCGROUP::build);

    public static final RegistryObject<Block> SOURCE_BLOCK = BLOCKS.register("source_block", () -> new SourceBlock(SOURCE_BLOCK_PROPERTIES));
    public static final RegistryObject<Block> TARGET_BLOCK = BLOCKS.register("target_block", () -> new TargetBlock(TARGET_BLOCK_PROPERTIES));
    public static final RegistryObject<Block> REDROUTER_BLOCK = BLOCKS.register("redrouter_block", () -> new RedRouterBlock(REDROUTER_BLOCK_PROPERTIES));
    public static final RegistryObject<Block> SCROLLER_BLOCK = BLOCKS.register("scroller_block", () -> new ScrollerBlock(SCROLLER_BLOCK_PROPERTIES));
    public static final RegistryObject<Block> ANIMATRONIC_BLOCK = BLOCKS.register("animatronic_block", AnimatronicBlock::new);

    public static final RegistryObject<TweakedBlockItem> SOURCE_BLOCK_ITEM = ITEMS.register("source_block", () -> new TweakedBlockItem(SOURCE_BLOCK.get(), SourceBlockPeripheral.getVersion()));
    public static final RegistryObject<TweakedBlockItem> TARGET_BLOCK_ITEM = ITEMS.register("target_block", () -> new TweakedBlockItem(TARGET_BLOCK.get(), TargetBlockPeripheral.getVersion()));
    public static final RegistryObject<TweakedBlockItem> REDROUTER_BLOCK_ITEM = ITEMS.register("redrouter_block", () -> new TweakedBlockItem(REDROUTER_BLOCK.get(), RedRouterBlockPeripheral.getVersion()));
    public static final RegistryObject<TweakedBlockItem> SCROLLER_BLOCK_ITEM = ITEMS.register("scroller_block", () -> new TweakedBlockItem(SCROLLER_BLOCK.get(), ScrollerBlockPeripheral.getVersion()));
    public static final RegistryObject<TweakedBlockItem> ANIMATRONIC_BLOCK_ITEM = ITEMS.register("animatronic_block", () -> new TweakedBlockItem(ANIMATRONIC_BLOCK.get(), AnimatronicPeripheral.getVersion()));

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE_BLOCK_ENTITY = BLOCK_ENTITIES.register("source_block_entity", () -> BlockEntityType.Builder.of(SourceBlockEntity::new, SOURCE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<TargetBlockEntity>> TARGET_BLOCK_ENTITY = BLOCK_ENTITIES.register("target_block_entity", () -> BlockEntityType.Builder.of(TargetBlockEntity::new, TARGET_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<RedRouterBlockEntity>> REDROUTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("redrouter_block_entity", () -> BlockEntityType.Builder.of(RedRouterBlockEntity::new, REDROUTER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<ScrollerBlockEntity>> SCROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("scroller_block_entity", () -> BlockEntityType.Builder.of(ScrollerBlockEntity::new, SCROLLER_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<AnimatronicBlockEntity>> ANIMATRONIC_BLOCK_ENTITY = BLOCK_ENTITIES.register("animatronic_block_entity", () -> BlockEntityType.Builder.of(AnimatronicBlockEntity::new, ANIMATRONIC_BLOCK.get()).build(null));

    public static final RegistryObject<SoundEvent> CAGE_LOCK_SOUND = SOUNDS.register("cage_lock", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCCBridge.MOD_ID, "cage_lock")));
    public static final RegistryObject<SoundEvent> CAGE_UNLOCK_SOUND = SOUNDS.register("cage_unlock", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(CCCBridge.MOD_ID, "cage_unlock")));

    public static void register() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        ENTITIES.register(modEventBus);
        SOUNDS.register(modEventBus);

        ITEMS.register(modEventBus);

        PAINTINGS.register("funny_redrouters", () -> new PaintingVariant(32,16));
        PAINTINGS.register(modEventBus);
        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static void registerCompat() {
        // Create stuff
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(CCCBridge.MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), CCCRegistries.SOURCE_BLOCK_ENTITY.get());
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(CCCBridge.MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), CCCRegistries.TARGET_BLOCK_ENTITY.get());

        AllInteractionBehaviours.registerBehaviour(CCCRegistries.ANIMATRONIC_BLOCK.getId(), new AnimatronicInteractionBehaviour());

        // ComputerCraft stuff
        ForgeComputerCraftAPI.registerPeripheralProvider(new TweakedPeripheralProvider());
    }
}
