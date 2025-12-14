package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.PeripheralBlockEntity;
import com.simibubi.create.foundation.data.CreateRegistrate;
import dan200.computercraft.api.peripheral.PeripheralLookup;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CCCBridge implements ModInitializer {
    public static final String MOD_ID = "cccbridge";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    public static final CreateRegistrate REGISTRATE = CreateRegistrate.create(CCCBridge.MOD_ID);

    @Override
    public void onInitialize() {
        CCCRegistries.register();
        CCCDisplayTargets.register();
        CCCDisplaySources.register();

        PeripheralLookup.get().registerFallback((world, pos, state, blockEntity, direction) -> {
            if (blockEntity instanceof PeripheralBlockEntity peripheral)
                return peripheral.getPeripheral(direction);
            return null;
        });

        REGISTRATE.register();

        LOGGER.info("The robots are spinning!");
    }

}
