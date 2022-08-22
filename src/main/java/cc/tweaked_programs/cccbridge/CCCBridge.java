package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.redrouter.RedRouterBlock;
import cc.tweaked_programs.cccbridge.block.redrouter.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.block.source.SourceBlock;
import cc.tweaked_programs.cccbridge.block.source.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.block.source.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.block.target.TargetBlock;
import cc.tweaked_programs.cccbridge.block.target.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.block.target.TargetBlockEntity;

import com.mojang.logging.LogUtils;
import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;

import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import org.slf4j.Logger;

import static cc.tweaked_programs.cccbridge.CCCBridge.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, MOD_ID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Block> SOURCE_BLOCK = BLOCKS.register("source_block", SourceBlock::new);
    public static final RegistryObject<Block> TARGET_BLOCK = BLOCKS.register("target_block", TargetBlock::new);
    public static final RegistryObject<Block> REDROUTER_BLOCK = BLOCKS.register("redrouter_block", RedRouterBlock::new);

    public static final RegistryObject<BlockEntityType<SourceBlockEntity>> SOURCE_BLOCK_ENTITY = BLOCK_ENTITIES.register("source_block_entity", () -> BlockEntityType.Builder.of(SourceBlockEntity::new, SOURCE_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<TargetBlockEntity>> TARGET_BLOCK_ENTITY = BLOCK_ENTITIES.register("target_block_entity", () -> BlockEntityType.Builder.of(TargetBlockEntity::new, TARGET_BLOCK.get()).build(null));
    public static final RegistryObject<BlockEntityType<RedRouterBlockEntity>> REDROUTER_BLOCK_ENTITY = BLOCK_ENTITIES.register("redrouter_block_entity", () -> BlockEntityType.Builder.of(RedRouterBlockEntity::new, REDROUTER_BLOCK.get()).build(null));

    public CCCBridge() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        BLOCK_ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());

        ITEMS.register("source_block", () -> new BlockItem(SOURCE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("target_block", () -> new BlockItem(TARGET_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register("redrouter_block", () -> new BlockItem(REDROUTER_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_REDSTONE)));
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(REDROUTER_BLOCK.get(), RenderType.translucent());
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), SOURCE_BLOCK_ENTITY.get().delegate);
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_source"), new TargetBlockDisplayTarget()), TARGET_BLOCK_ENTITY.get().delegate);

            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, SOURCE_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, TARGET_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, REDROUTER_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
        });
    }
}
