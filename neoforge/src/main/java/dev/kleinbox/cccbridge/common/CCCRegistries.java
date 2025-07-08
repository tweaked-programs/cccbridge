package dev.kleinbox.cccbridge.common;

import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.simibubi.create.api.behaviour.display.DisplayTarget;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.api.registry.CreateRegistries;
import dan200.computercraft.api.peripheral.PeripheralCapability;
import dev.kleinbox.cccbridge.common.computercraft.peripherals.*;
import dev.kleinbox.cccbridge.common.create.behaviour.AnimatronicInteractionBehaviour;
import dev.kleinbox.cccbridge.common.create.display.SourceBlockDisplaySource;
import dev.kleinbox.cccbridge.common.create.display.TargetBlockDisplayTarget;
import dev.kleinbox.cccbridge.common.minecraft.TweakedBlockItem;
import dev.kleinbox.cccbridge.common.minecraft.block.*;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.*;
import dev.kleinbox.cccbridge.common.modloader.CCCBridge;
import dev.kleinbox.cccbridge.common.modloader.PropertiesBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

import static dev.kleinbox.cccbridge.common.minecraft.block.RedRouterBlock.REDROUTER_BLOCK_PROPERTIES;
import static dev.kleinbox.cccbridge.common.minecraft.block.ScrollerBlock.SCROLLER_BLOCK_PROPERTIES;
import static dev.kleinbox.cccbridge.common.minecraft.block.SourceBlock.SOURCE_BLOCK_PROPERTIES;
import static dev.kleinbox.cccbridge.common.minecraft.block.TargetBlock.TARGET_BLOCK_PROPERTIES;

@SuppressWarnings({"DataFlowIssue", "unused"})
public class CCCRegistries {
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(BuiltInRegistries.BLOCK, CCCBridge.MOD_ID);
    private static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT, CCCBridge.MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, CCCBridge.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM, CCCBridge.MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CCCBridge.MOD_ID);
    public static final DeferredRegister<DisplaySource> CREATE_SOURCES = DeferredRegister.create(CreateRegistries.DISPLAY_SOURCE, CCCBridge.MOD_ID);
    public static final DeferredRegister<DisplayTarget> CREATE_TARGETS = DeferredRegister.create(CreateRegistries.DISPLAY_TARGET, CCCBridge.MOD_ID);

    public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("cccbridge_group", PropertiesBuilder.CCCGROUP::build);

    public static final Supplier<SourceBlockDisplaySource> SOURCE_BLOCK_SOURCE = CREATE_SOURCES.register("source_block_display_source", SourceBlockDisplaySource::new);
    public static final Supplier<TargetBlockDisplayTarget> TARGET_BLOCK_TARGET = CREATE_TARGETS.register("target_block_display_target", TargetBlockDisplayTarget::new);

    public static final Supplier<Block> SOURCE_BLOCK = BLOCKS.register("source_block", () -> new SourceBlock(SOURCE_BLOCK_PROPERTIES));
    public static final Supplier<Block> TARGET_BLOCK = BLOCKS.register("target_block", () -> new TargetBlock(TARGET_BLOCK_PROPERTIES));
    public static final Supplier<Block> REDROUTER_BLOCK = BLOCKS.register("redrouter_block", () -> new RedRouterBlock(REDROUTER_BLOCK_PROPERTIES));
    public static final Supplier<Block> SCROLLER_BLOCK = BLOCKS.register("scroller_block", () -> new ScrollerBlock(SCROLLER_BLOCK_PROPERTIES));
    public static final Supplier<Block> ANIMATRONIC_BLOCK = BLOCKS.register("animatronic_block", () -> new AnimatronicBlock(BlockBehaviour.Properties.of().strength(1.3f).sound(SoundType.CHAIN)));

    public static final Supplier<TweakedBlockItem> SOURCE_BLOCK_ITEM = ITEMS.register("source_block", () -> new TweakedBlockItem(SOURCE_BLOCK.get(), SourceBlockPeripheral.getVersion()));
    public static final Supplier<TweakedBlockItem> TARGET_BLOCK_ITEM = ITEMS.register("target_block", () -> new TweakedBlockItem(TARGET_BLOCK.get(), TargetBlockPeripheral.getVersion()));
    public static final Supplier<TweakedBlockItem> REDROUTER_BLOCK_ITEM = ITEMS.register("redrouter_block", () -> new TweakedBlockItem(REDROUTER_BLOCK.get(), RedRouterBlockPeripheral.getVersion()));
    public static final Supplier<TweakedBlockItem> SCROLLER_BLOCK_ITEM = ITEMS.register("scroller_block", () -> new TweakedBlockItem(SCROLLER_BLOCK.get(), ScrollerBlockPeripheral.getVersion()));
    public static final Supplier<TweakedBlockItem> ANIMATRONIC_BLOCK_ITEM = ITEMS.register("animatronic_block", () -> new TweakedBlockItem(ANIMATRONIC_BLOCK.get(), AnimatronicPeripheral.getVersion()));

    public static final Supplier<BlockEntityType<SourceBlockEntity>> SOURCE_BLOCK_ENTITY = BLOCK_ENTITIES.register("source_block_entity", () -> BlockEntityType.Builder.of(SourceBlockEntity::new, SOURCE_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<TargetBlockEntity>> TARGET_BLOCK_ENTITY = BLOCK_ENTITIES.register("target_block_entity", () -> BlockEntityType.Builder.of(TargetBlockEntity::new, TARGET_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<RedRouterBlockEntity>> REDROUTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("redrouter_block_entity", () -> BlockEntityType.Builder.of(RedRouterBlockEntity::new, REDROUTER_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<ScrollerBlockEntity>> SCROLLER_BLOCK_ENTITY = BLOCK_ENTITIES.register("scroller_block_entity", () -> BlockEntityType.Builder.of(ScrollerBlockEntity::new, SCROLLER_BLOCK.get()).build(null));
    public static final Supplier<BlockEntityType<AnimatronicBlockEntity>> ANIMATRONIC_BLOCK_ENTITY = BLOCK_ENTITIES.register("animatronic_block_entity", () -> BlockEntityType.Builder.of(AnimatronicBlockEntity::new, ANIMATRONIC_BLOCK.get()).build(null));

    public static final Supplier<SoundEvent> CAGE_LOCK_SOUND = SOUNDS.register("cage_lock", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "cage_lock")));
    public static final Supplier<SoundEvent> CAGE_UNLOCK_SOUND = SOUNDS.register("cage_unlock", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CCCBridge.MOD_ID, "cage_unlock")));

    public static void register(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        SOUNDS.register(modEventBus);

        CREATE_SOURCES.register(modEventBus);
        CREATE_TARGETS.register(modEventBus);

        ITEMS.register(modEventBus);

        CREATIVE_MODE_TABS.register(modEventBus);
    }

    public static void registerCompat() {
        // Create stuff
        MovingInteractionBehaviour.REGISTRY.register(CCCRegistries.ANIMATRONIC_BLOCK.get(), new AnimatronicInteractionBehaviour());
        DisplaySource.BY_BLOCK_ENTITY.add(SOURCE_BLOCK_ENTITY.get(), SOURCE_BLOCK_SOURCE.get());
        DisplayTarget.BY_BLOCK_ENTITY.register(TARGET_BLOCK_ENTITY.get(), TARGET_BLOCK_TARGET.get());
    }

    public void registerCapabilities(RegisterCapabilitiesEvent event) {
        BLOCK_ENTITIES.getEntries().forEach((entry) -> {
            event.registerBlockEntity(
                    PeripheralCapability.get(),
                    entry.get(),
                    (be, side) -> {
                        if (be instanceof PeripheralBlockEntity provider)
                            return provider.getPeripheral(side);
                        return null;
                    }
            );
        });
    }
}
