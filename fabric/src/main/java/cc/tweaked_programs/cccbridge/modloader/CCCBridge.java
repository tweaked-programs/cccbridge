package cc.tweaked_programs.cccbridge.modloader;

import cc.tweaked_programs.cccbridge.Registries;
import cc.tweaked_programs.cccbridge.computercraft.PeripheralProvider;
import cc.tweaked_programs.cccbridge.create.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.create.display.TargetBlockDisplayTarget;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCCBridge implements ModInitializer {
    public static final String MOD_ID = "cccbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        // Minecraft stuff
        Registries.register();

        // Create stuff
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), (BlockEntityType<? extends BlockEntity>) Registries.SOURCE_BLOCK_ENTITY.get());
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), (BlockEntityType<? extends BlockEntity>) Registries.TARGET_BLOCK_ENTITY.get());

        // ComputerCraft stuff
        ComputerCraftAPI.registerPeripheralProvider(new PeripheralProvider());
    }
}
