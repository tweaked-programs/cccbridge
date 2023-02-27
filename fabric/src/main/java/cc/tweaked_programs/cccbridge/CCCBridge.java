package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.*;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.decoration.PaintingVariant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCCBridge implements ModInitializer {
    public static final String MOD_ID = "cccbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final IPeripheralProvider peripheralProvider = new PeripheralProvider();

    @Override
    public void onInitialize() {
        // Block (-entities)
        BlockRegister.registerBlockEntity("source_block", SourceBlockEntity::new, new SourceBlock());
        BlockRegister.registerBlockEntity("target_block", TargetBlockEntity::new, new TargetBlock());

        BlockRegister.registerBlockEntity("redrouter_block", RedRouterBlockEntity::new, new RedRouterBlock());
        BlockRegister.registerBlockEntity("scroller_block", ScrollerBlockEntity::new, new ScrollerBlock());

        // Create Display Stuff
        AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), BlockRegister.getBlockEntityType("source_block"));
        AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new ResourceLocation(MOD_ID, "target_block_display_target"), new TargetBlockDisplayTarget()), BlockRegister.getBlockEntityType("target_block"));

        // Misc
        Registry.register(Registry.PAINTING_VARIANT, new ResourceLocation(MOD_ID, "funny_redrouters"), new PaintingVariant(32,16));
        CCCSoundEvents.init();
        ComputerCraftAPI.registerPeripheralProvider(peripheralProvider);
    }
}
