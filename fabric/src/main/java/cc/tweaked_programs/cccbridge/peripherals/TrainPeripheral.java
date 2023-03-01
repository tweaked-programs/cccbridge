package cc.tweaked_programs.cccbridge.peripherals;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.Create;
import com.simibubi.create.content.logistics.trains.GraphLocation;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.GlobalStation;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.TrainEditPacket.TrainEditReturnPacket;
import com.simibubi.create.content.logistics.trains.management.schedule.Schedule;
import com.simibubi.create.foundation.networking.AllPackets;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;

/**
 * This peripheral is provided for the Train Station from Create. It is used to get general data of it and its trains and give some control over it too.
 *
 * @version 1.0
 */
public class TrainPeripheral implements IPeripheral {
    private static Schedule schedule;
    private final Level level;
    private final StationTileEntity station;

    public TrainPeripheral(@NotNull BlockPos pos, Level level) {
        this.level = level;
        station = (StationTileEntity) level.getBlockEntity(pos);
    }


    @NotNull
    @Override
    public String getType() {
        return "train_station";
    }

    /**
     * Assembles a train.
     *
     * @return Whether it was successful or not with a message.
     */
    @LuaFunction
    public final MethodResult assemble() {
        if (station.getStation().getPresentTrain() != null) {
            return MethodResult.of(false, "There is a assembled Train");
        }
        if (station.tryEnterAssemblyMode()) {
            station.assemble(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"));
            station.tick();
            if (this.schedule == null) {
                return MethodResult.of(false, "No Schedule saved");
            }
            station.getStation().getPresentTrain().runtime.setSchedule(this.schedule, true);
            this.schedule = null;
            return MethodResult.of(true, "Train assembled");
        }
        return MethodResult.of(false, "Can't assemble Train");
    }

    /**
     * Disassembles a train.
     *
     * @return Whether it was successful or not with a message.
     */
    @LuaFunction
    public final MethodResult disassemble() {
        if (station.getStation().getPresentTrain() == null) {
            return MethodResult.of(false, "there is no Train");
        }
        if (station.getStation().getPresentTrain().canDisassemble()) {
            Direction direction = station.getAssemblyDirection();
            BlockPos position = station.edgePoint.getGlobalPosition().above();
            this.schedule = station.getStation().getPresentTrain().runtime.getSchedule();
            ServerPlayer player = new ServerPlayer(
                    level.getServer(),
                    level.getServer().overworld(),
                    new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"),
                    null
            );
            station.getStation().getPresentTrain().disassemble(player, direction, position);
            return MethodResult.of(true, "Train disassembled");
        }
        return MethodResult.of(false, "Can't disassemble Train");
    }

    /**
     * Returns the current station name.
     *
     * @return Name of station.
     */
    @LuaFunction
    public String getStationName() {
        return station.getStation().name;
    }

    /**
     * Returns the current trains name.
     *
     * @return Name of train.
     */
    @LuaFunction
    public String getTrainName() {
        return Objects.requireNonNull(station.getStation().getPresentTrain()).name.getString();
    }

    /**
     * Sets the stations name
     *
     * @param name The new name.
     * @return Whether it was successful or not.
     */
    @LuaFunction
    public final boolean setStationName(@NotNull String name) {
        GlobalStation station2 = station.getStation();
        GraphLocation graphLocation = station.edgePoint.determineGraphLocation();
        if (station2 != null && graphLocation != null) {
            station2.name = name;
            Create.RAILWAYS.sync.pointAdded(graphLocation.graph, station2);
            Create.RAILWAYS.markTracksDirty();
            station.notifyUpdate();
            return true;
        }
        //AllPackets.channel.sendToServer(StationEditPacket.configure(station.getBlockPos(),false,name));
        return false;
    }

    /**
     * Sets the current trains name.
     *
     * @param name The new name.
     * @return Whether it was successful or not with a message.
     */
    @LuaFunction
    public final MethodResult setTrainName(@NotNull String name) {
        if (station.getStation().getPresentTrain() == null) {
            return MethodResult.of(false, "There is no train to set the name of");
        }
        Train train = station.getStation().getPresentTrain();
        Train Train = Create.RAILWAYS.sided(level).trains.get(train.id);
        if (Train == null) {
            return MethodResult.of(false, "Train not found");
        }
        if (!name.isBlank()) {
            Train.name = Component.literal(name);
            station.tick();
            AllPackets.channel.sendToClientsInServer(new TrainEditReturnPacket(train.id, name, Train.icon.getId()), level.getServer());
            return MethodResult.of(true, "Train name set to " + name);
        }
        //AllPackets.channel.sendToServer(new TrainEditPacket(train.id, name, train.icon.getId()));
        return MethodResult.of(false, "Train name cannot be blank");
    }

    /**
     * Gets the number of Bogeys attached to the current train.
     *
     * @return The number of Bogeys.
     */
    @LuaFunction
    public int getBogeys() {
        if (station.getStation().getPresentTrain() == null) {
            return 0;
        }
        return station.getStation().getPresentTrain().carriages.size();
    }

    /**
     * Returns boolean whether a train os present or not.
     *
     * @return Whether it was successful or not.
     */
    @LuaFunction
    public boolean getPresentTrain() {
        return station.getStation().getPresentTrain() != null;
    }

    /**
     * Clears the schedule saved in the station.
     */
    @LuaFunction
    public void clearSchedule() {
        this.schedule = null;
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this == iPeripheral;
    }
}