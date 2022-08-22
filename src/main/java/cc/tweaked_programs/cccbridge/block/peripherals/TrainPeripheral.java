package cc.tweaked_programs.cccbridge.block.peripherals;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationEditPacket;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.TrainEditPacket;
import com.simibubi.create.content.logistics.trains.management.schedule.Schedule;
import com.simibubi.create.foundation.networking.AllPackets;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TrainPeripheral implements IPeripheral {

    private final List<IComputerAccess> connectedComputers = new ArrayList<>();
    private final BlockPos pos;
    private final World level;
    private final StationTileEntity station;
    private static Schedule schedule;

    public TrainPeripheral(@NotNull BlockPos pos, World level) {
        this.pos = pos;
        this.level = level;
        this.station = (StationTileEntity) level.getBlockEntity(pos);
    }


    @NotNull
    @Override
    public String getType() {
        return "train_station";
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        connectedComputers.remove(computer);
    }

    //assembles the train
    @LuaFunction
    public final MethodResult assemble() {
        if (station.getStation().getPresentTrain() != null) {
            return MethodResult.of(false,"there is a assembled Train");
        }
        if (station.tryEnterAssemblyMode()) {
            station.assemble(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"));
            station.tick();
            if (this.schedule == null) {
                return MethodResult.of(false,"No Schedule Saved");
            }
            station.getStation().getPresentTrain().runtime.setSchedule(this.schedule, true);
            this.schedule = null;
            return MethodResult.of(true, "Train Assembled");
        }
        return MethosResult.of(false, "can't assemble Train");
    }

    //disassembles the train
    @LuaFunction
    public boolean disassemble() {
        if (station.getStation().getPresentTrain() == null) {
            return false;
        }
        if (station.getStation().getPresentTrain().canDisassemble()) {
            Direction direction = station.getAssemblyDirection();
            BlockPos position = station.edgePoint.getGlobalPosition().up();
            this.schedule = station.getStation().getPresentTrain().runtime.getSchedule();
            ServerPlayerEntity player = new ServerPlayerEntity(level.getServer(), level.getServer().getOverworld(), new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"));
            station.getStation().getPresentTrain().disassemble(player, direction, position);
            return true;
        }
        return false;
    }

    //returns the stations name
    @LuaFunction
    public String getStationName() {
        return station.getStation().name;
    }

    //returns the train's name
    @LuaFunction
    public String getTrainName() {
        return Objects.requireNonNull(station.getStation().getPresentTrain()).name.getString();
    }

    //sets the Stations name
    @LuaFunction
    public boolean setStationName(@NotNull String name) {
        AllPackets.channel.sendToServer(StationEditPacket.configure(station.getPos(), false, name));
        return true;
    }

    //sets the Trains name
    @LuaFunction
    public boolean setTrainName(@NotNull String name) {
        if (station.getStation().getPresentTrain() == null) {
            return false;
        }
        Train train = station.getStation().getPresentTrain();
        AllPackets.channel.sendToServer(new TrainEditPacket(train.id, name, train.icon.getId()));
        return true;
    }

    //gets the Number of Bogeys atteched to the Train
    @LuaFunction
    public int getBogeys() {
        if (station.getStation().getPresentTrain() == null) {
            return 0;
        }
        return station.getStation().getPresentTrain().carriages.size();
    }

    //gets if there is a train present
    @LuaFunction
    public boolean getPresentTrain() {
        return station.getStation().getPresentTrain() != null;
    }

    //Clears the schedule saved in the station
    @LuaFunction
    public boolean clearSchedule() {
        this.schedule = null;
        return true;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        connectedComputers.add(computer);
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this == iPeripheral;
    }
}