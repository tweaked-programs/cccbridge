package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import dan200.computercraft.api.ForgeComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLLoadCompleteEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModFileInfo;
import net.minecraftforge.forgespi.locating.IModFile;

import static cc.tweaked_programs.cccbridge.CCCBridge.MOD_ID;

@Mod(MOD_ID)
@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class CCCBridge {
    public static final String MOD_ID = "cccbridge";
    //private static final Logger LOGGER = LogUtils.getLogger();

    public CCCBridge() {
        BlockRegister.register();
    }

    @SubscribeEvent
    public static void complete(FMLLoadCompleteEvent event) {
        event.enqueueWork(() -> {
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), BlockRegister.SOURCE_BLOCK_ENTITY.get());
            AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), BlockRegister.TARGET_BLOCK_ENTITY.get());

            ForgeComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, BlockRegister.SOURCE_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ForgeComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos, BlockRegister.TARGET_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ForgeComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos,BlockRegister.REDROUTER_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ForgeComputerCraftAPI.registerPeripheralProvider((world, pos, side) -> world.getBlockEntity(pos,BlockRegister.SCROLLER_BLOCK_ENTITY.get()).map(be -> be.getPeripheral(side)).map(val -> LazyOptional.of(() -> val)).orElse(LazyOptional.empty()));
            ForgeComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
        });
    }
}
