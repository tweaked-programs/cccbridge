package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import static cc.tweaked_programs.cccbridge.CCCBridge.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    //private static final Logger LOGGER = LogUtils.getLogger();

    public static IPeripheralProvider peripheralProvider = new PeripheralProvider();

    public CCCBridge() {
        BlockRegister.register();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), BlockRegister.SOURCE_BLOCK_ENTITY.get().delegate);
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_source"), new TargetBlockDisplayTarget()), BlockRegister.TARGET_BLOCK_ENTITY.get().delegate);

            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, BlockRegister.SOURCE_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, BlockRegister.TARGET_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos,BlockRegister.REDROUTER_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos,BlockRegister.SCROLLER_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ComputerCraftAPI.registerPeripheralProvider(peripheralProvider);
        });
    }

    private void clientSetup(final FMLClientSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(BlockRegister.REDROUTER_BLOCK.get(), RenderType.translucent());
        ItemBlockRenderTypes.setRenderLayer(BlockRegister.SCROLLER_BLOCK.get(), RenderType.translucent());
    }
}
