package cc.tweaked_programs.cccbridge.peripherals;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.Create;
import com.simibubi.create.content.contraptions.base.KineticTileEntity;
import com.simibubi.create.content.contraptions.relays.advanced.SpeedControllerTileEntity;
import com.simibubi.create.content.logistics.trains.GraphLocation;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.GlobalStation;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.TrainEditPacket.TrainEditReturnPacket;
import com.simibubi.create.content.logistics.trains.management.schedule.Schedule;
import com.simibubi.create.foundation.config.AllConfigs;
import com.simibubi.create.foundation.networking.AllPackets;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

public class SpeedControllerPeripheral implements IPeripheral {
    private final World level;
    private final SpeedControllerTileEntity controller;
    private final Integer maxSpeed = AllConfigs.SERVER.kinetics.maxRotationSpeed.get();

    public SpeedControllerPeripheral(@NotNull BlockPos pos, World level) {
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