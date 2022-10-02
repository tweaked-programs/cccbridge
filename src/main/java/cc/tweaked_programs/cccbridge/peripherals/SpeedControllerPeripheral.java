package cc.tweaked_programs.cccbridge.peripherals;

import com.simibubi.create.content.contraptions.relays.advanced.SpeedControllerTileEntity;
import com.simibubi.create.foundation.config.AllConfigs;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SpeedControllerPeripheral implements IPeripheral {
    private final Level level;
    private final SpeedControllerTileEntity controller;
    private final Integer maxSpeed = AllConfigs.SERVER.kinetics.maxRotationSpeed.get();

    public SpeedControllerPeripheral(@NotNull BlockPos pos, Level level) {
        this.level = level;
        controller = (SpeedControllerTileEntity) level.getBlockEntity(pos);
    }


    @NotNull
    @Override
    public String getType() {
        return "speed_controller";
    }

    /**
     * Returns the theoretical current wanted speed.
     * @return Current speed
     */
    @LuaFunction
    public final float getSpeed() {
        return controller.getSpeed();
    }

    /**
     * Changes the current speed to a new value. (must be between from server given interval)
     * @param rot New wanted speed
     * @throws LuaException When value is out of bounds.
     */
    @LuaFunction
    public final void setSpeed(int rot) throws LuaException {
        /*if (rot < -maxSpeed || rot > maxSpeed)
            throw new LuaException("Given rotation must be between "+-maxSpeed+" & "+maxSpeed+"!");

        float prevSpeed = controller.getSpeed();
        controller.setSpeed(rot);

        controller.markDirty();
        controller.onSpeedChanged(prevSpeed);
        controller.sendData();*/
    }

    /**
     * Whenever the Speed Controller is OverStressed or not.
     * @return State of being overstressed or not
     */
    @LuaFunction
    public final boolean isOverStressed() {
        return controller.isOverStressed();
    }


    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this == iPeripheral;
    }
}