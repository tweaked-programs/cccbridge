package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.assistance.misc.RegisterEntry;
import cc.tweaked_programs.cccbridge.computercraft.peripherals.*;
import cc.tweaked_programs.cccbridge.minecraft.TweakedBlockItem;
import cc.tweaked_programs.cccbridge.minecraft.block.*;
import cc.tweaked_programs.cccbridge.minecraft.blockEntity.*;
import cc.tweaked_programs.cccbridge.minecraft.entity.animatronic.AnimatronicEntity;
import cc.tweaked_programs.cccbridge.modloader.CCCBridge;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.Arrays;
import java.util.List;

import static cc.tweaked_programs.cccbridge.minecraft.block.RedRouterBlock.REDROUTER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.minecraft.block.ScrollerBlock.SCROLLER_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.minecraft.block.SourceBlock.SOURCE_BLOCK_PROPERTIES;
import static cc.tweaked_programs.cccbridge.minecraft.block.TargetBlock.TARGET_BLOCK_PROPERTIES;

public enum Registries {
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

    ANIMATRONIC_ENTITY("animatronic", EntityType.Builder.of(AnimatronicEntity::new, MobCategory.MISC).sized(0.6F, 1.8F).build(new ResourceLocation(CCCBridge.MOD_ID, "animatronic").toString())),

    CAGE_LOCK_SOUND("cage_lock"),
    CAGE_UNLOCK_SOUND("cage_unlock"),

    FUNNY_REDROUTERS_PAINTING("funny_redrouters", new PaintingVariant(32,16));

    public static void register() {
        for (Registries entry : Registries.ENTRIES) {
            switch (entry.type()) {
                case BLOCK -> Registry.register(Registry.BLOCK,entry.resourceLocation(), (Block)entry.get());
                case ITEM -> Registry.register(Registry.ITEM, entry.resourceLocation(), (Item)entry.get());
                case BLOCK_ENTITY -> Registry.register(Registry.BLOCK_ENTITY_TYPE, entry.resourceLocation(),  (BlockEntityType<? extends BlockEntity>)entry.get());
                case ENTITY -> Registry.register(Registry.ENTITY_TYPE, entry.resourceLocation(), (EntityType<? extends Entity>)entry.get());
                case SOUND_EVENT -> Registry.register(Registry.SOUND_EVENT, entry.resourceLocation(), (SoundEvent)entry.get());
                case PAINTING_VARIANT -> Registry.register(Registry.PAINTING_VARIANT, entry.resourceLocation(), (PaintingVariant)entry.get());
            }
        }
    }

    public static final List<Registries> ENTRIES = Arrays.asList(Registries.values());

    private final RegisterEntry entry;

    Registries(String id, Block entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.BLOCK);
    }

    <T extends BlockEntity> Registries(String id, BlockEntityType<T> entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.BLOCK_ENTITY);
    }

    <T extends Entity> Registries(String id, EntityType<T> entry) {
        this.entry = new RegisterEntry(id, entry, RegisterEntry.TYPE.ENTITY);
    }

    Registries(RegisterEntry block, double version) {
        this.entry = new RegisterEntry(block.id(), new TweakedBlockItem((Block)block.entry(), version), RegisterEntry.TYPE.ITEM);
    }

    Registries(String id) {
        this.entry = new RegisterEntry(id, new SoundEvent(new ResourceLocation(CCCBridge.MOD_ID, id)), RegisterEntry.TYPE.SOUND_EVENT);
    }

    Registries(String id, PaintingVariant entry) {
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
