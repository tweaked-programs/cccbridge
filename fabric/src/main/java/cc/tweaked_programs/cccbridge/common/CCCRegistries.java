package cc.tweaked_programs.cccbridge.common;

import cc.tweaked_programs.cccbridge.common.assistance.RegisterEntry;
import cc.tweaked_programs.cccbridge.common.computercraft.peripherals.*;
import cc.tweaked_programs.cccbridge.common.create.behaviour.AnimatronicInteractionBehaviour;
import cc.tweaked_programs.cccbridge.common.minecraft.TweakedBlockItem;
import cc.tweaked_programs.cccbridge.common.minecraft.block.*;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.*;
import cc.tweaked_programs.cccbridge.common.modloader.CCCBridge;
import cc.tweaked_programs.cccbridge.common.modloader.PropertiesBuilder;
import com.simibubi.create.AllInteractionBehaviours;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.List;

import static cc.tweaked_programs.cccbridge.common.minecraft.block.RedRouterBlock.REDROUTER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.ScrollerBlock.SCROLLER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.SourceBlock.SOURCE_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.common.minecraft.block.TargetBlock.TARGET_BLOCK_PROPERTIES;

public enum CCCRegistries {
    SOURCE_BLOCK("source_block", new SourceBlock(SOURCE_BLOCK_PROPERTIES)),
    TARGET_BLOCK("target_block", new TargetBlock(TARGET_BLOCK_PROPERTIES)),
    REDROUTER_BLOCK("redrouter_block", new RedRouterBlock(REDROUTER_BLOCK_PROPERTIES)),
    SCROLLER_BLOCK("scroller_block", new ScrollerBlock(SCROLLER_BLOCK_PROPERTIES)),
    ANIMATRONIC_BLOCK("animatronic_block", new AnimatronicBlock()),

    SOURCE_BLOCK_ITEM(SOURCE_BLOCK.entry, SourceBlockPeripheral.getVersion()),
    TARGET_BLOCK_ITEM(TARGET_BLOCK.entry, TargetBlockPeripheral.getVersion()),
    REDROUTER_BLOCK_ITEM(REDROUTER_BLOCK.entry, RedRouterBlockPeripheral.getVersion()),
    SCROLLER_BLOCK_ITEM(SCROLLER_BLOCK.entry, ScrollerBlockPeripheral.getVersion()),
    ANIMATRONIC_BLOCK_ITEM(ANIMATRONIC_BLOCK.entry, AnimatronicPeripheral.getVersion()),

    SOURCE_BLOCK_ENTITY("source_block_entity", BlockEntityType.Builder.of(SourceBlockEntity::new, (SourceBlock)SOURCE_BLOCK.get()).build(null)),
    TARGET_BLOCK_ENTITY("target_block_entity", BlockEntityType.Builder.of(TargetBlockEntity::new, (TargetBlock)TARGET_BLOCK.get()).build(null)),
    REDROUTER_BLOCK_ENTITY("redrouter_block_entity", BlockEntityType.Builder.of(RedRouterBlockEntity::new, (RedRouterBlock)REDROUTER_BLOCK.get()).build(null)),
    SCROLLER_BLOCK_ENTITY("scroller_block_entity", BlockEntityType.Builder.of(ScrollerBlockEntity::new, (ScrollerBlock)SCROLLER_BLOCK.get()).build(null)),
    ANIMATRONIC_BLOCK_ENTITY("animatronic_block_entity", BlockEntityType.Builder.of(AnimatronicBlockEntity::new, (AnimatronicBlock)ANIMATRONIC_BLOCK.get()).build(null)),

    CAGE_LOCK_SOUND("cage_lock"),
    CAGE_UNLOCK_SOUND("cage_unlock"),

    FUNNY_REDROUTERS_PAINTING("funny_redrouters", new PaintingVariant(32,16));

    public static final ResourceKey<CreativeModeTab> CCCGROUP = ResourceKey.create(Registries.CREATIVE_MODE_TAB, new ResourceLocation(CCCBridge.MOD_ID, "main_group"));

    public static void register() {
        Registry.register(BuiltInRegistries.CREATIVE_MODE_TAB, CCCGROUP, PropertiesBuilder.CCCGROUP);

        for (CCCRegistries entry : CCCRegistries.ENTRIES) {
            switch (entry.type()) {
                case BLOCK -> Registry.register(BuiltInRegistries.BLOCK, entry.resourceLocation(), (Block)entry.get());
                case ITEM -> {
                    Item item = (Item)entry.get();
                    Registry.register(BuiltInRegistries.ITEM, entry.resourceLocation(), item);
                    // Add to item group
                    ItemGroupEvents.modifyEntriesEvent(CCCGROUP).register(content -> {
                        content.accept(item);
                    });
                }
                case BLOCK_ENTITY -> Registry.register(BuiltInRegistries.BLOCK_ENTITY_TYPE, entry.resourceLocation(),  (BlockEntityType<? extends BlockEntity>)entry.get());
                case SOUND_EVENT -> Registry.register(BuiltInRegistries.SOUND_EVENT, entry.resourceLocation(), (SoundEvent)entry.get());
                case PAINTING_VARIANT -> Registry.register(BuiltInRegistries.PAINTING_VARIANT, entry.resourceLocation(), (PaintingVariant)entry.get());
            }
        }

        AllInteractionBehaviours.registerBehaviour(ANIMATRONIC_BLOCK.resourceLocation(), new AnimatronicInteractionBehaviour());
    }

    public static final List<CCCRegistries> ENTRIES = Arrays.asList(CCCRegistries.values());

    private final RegisterEntry entry;

    CCCRegistries(String id, Block entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.BLOCK);
    }

    <T extends BlockEntity> CCCRegistries(String id, BlockEntityType<T> entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.BLOCK_ENTITY);
    }

    CCCRegistries(RegisterEntry block, double version) {
        this.entry = new RegisterEntry(block.id(), new TweakedBlockItem((Block)block.entry(), version), RegisterEntry.TYPE.ITEM);
    }

    CCCRegistries(String id) {
        this.entry = new RegisterEntry(id, SoundEvent.createVariableRangeEvent(new ResourceLocation(CCCBridge.MOD_ID, id)), RegisterEntry.TYPE.SOUND_EVENT);
    }

    CCCRegistries(String id, PaintingVariant entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.PAINTING_VARIANT);
    }

    public Object get() {
        return entry.entry();
    }

    public RegisterEntry.TYPE type() {
        return entry.type();
    }

    public String id() {
        return entry.id();
    }

    public ResourceLocation resourceLocation() {
        return new ResourceLocation(CCCBridge.MOD_ID, entry.id());
    }
}
