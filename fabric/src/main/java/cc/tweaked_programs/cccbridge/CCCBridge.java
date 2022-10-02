package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.block.RedRouterBlock;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.block.SourceBlock;
import cc.tweaked_programs.cccbridge.display.SourceBlockDisplaySource;
import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.block.TargetBlock;
import cc.tweaked_programs.cccbridge.display.TargetBlockDisplayTarget;
import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import com.simibubi.create.content.logistics.block.display.AllDisplayBehaviours;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.fabricmc.api.ModInitializer;
import net.minecraft.util.Identifier;

public class CCCBridge implements ModInitializer {
    public static final String MOD_ID = "cccbridge";
    // public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final IPeripheralProvider peripheralProvider = new PeripheralProvider();

    @Override
    public void onInitialize() {
        BlockRegister.registerBlockEntity("source_block", SourceBlockEntity::new, new SourceBlock());
        AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new Identifier(MOD_ID, "source_block_display_source"), new SourceBlockDisplaySource()), BlockRegister.getBlockEntityType("source_block"));

        BlockRegister.registerBlockEntity("target_block", TargetBlockEntity::new, new TargetBlock());
        AllDisplayBehaviours.assignTile(AllDisplayBehaviours.register(new Identifier(MOD_ID, "target_block_display_source"), new TargetBlockDisplayTarget()), BlockRegister.getBlockEntityType("target_block"));

        BlockRegister.registerBlockEntity("redrouter_block", RedRouterBlockEntity::new, new RedRouterBlock());

        ComputerCraftAPI.registerPeripheralProvider(peripheralProvider);
    }
}
