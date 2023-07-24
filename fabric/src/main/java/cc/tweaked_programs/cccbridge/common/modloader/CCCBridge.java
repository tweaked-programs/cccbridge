package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.create.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.common.create.display.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.PeripheralBlockEntity;
import com.simibubi.create.content.redstone.displayLink.AllDisplayBehaviours;
import dan200.computercraft.api.peripheral.PeripheralLookup;
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
        CCCRegistries.register();

        // Create stuff
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), (BlockEntityType<? extends BlockEntity>) CCCRegistries.SOURCE_BLOCK_ENTITY.get());
        AllDisplayBehaviours.assignBlockEntity(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), (BlockEntityType<? extends BlockEntity>) CCCRegistries.TARGET_BLOCK_ENTITY.get());

        // ComputerCraft stuff
        PeripheralLookup.get().registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof PeripheralBlockEntity peripheralBlockEntity)
                return peripheralBlockEntity.getPeripheral(direction);
            return null;
        });
    }
}
