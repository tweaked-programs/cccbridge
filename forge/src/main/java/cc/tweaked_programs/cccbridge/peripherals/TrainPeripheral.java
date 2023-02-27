package cc.tweaked_programs.cccbridge.peripherals;

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
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;


public class TrainPeripheral implements IPeripheral {
    private static Schedule schedule;
    private final List<IComputerAccess> connectedComputers = new ArrayList<>();
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
            return MethodResult.of(false, "Train already assembled");
        }
        if (station.tryEnterAssemblyMode()) {
            station.assemble(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"));
            station.tick();
            if (schedule == null) {
                return MethodResult.of(true, "No schedule found");
            }
            station.getStation().getPresentTrain().runtime.setSchedule(schedule, true);
            schedule = null;
            return MethodResult.of(true, "Train assembled");
        }
        return MethodResult.of(false, "Could not assemble train");
    }

    /**
     * Disassembles a train.
     *
     * @return Whether it was successful or not with a message.
     */
    @LuaFunction
    public final MethodResult disassemble() {
        if (station.getStation().getPresentTrain() == null) {
            return MethodResult.of(false, "Train not assembled");
        }
        if (station.getStation().getPresentTrain().canDisassemble()) {
            Direction direction = station.getAssemblyDirection();
            BlockPos position = station.edgePoint.getGlobalPosition().above();
            this.schedule = station.getStation().getPresentTrain().runtime.getSchedule();
            /*ServerPlayer player = new ServerPlayer(
                    level.getServer(),
                    level.getServer().overworld(),
                    new GameProfile(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"), "Notch"),
                    null
            );*/
            station.getStation().getPresentTrain().disassemble(/*player,*/ direction, position);
            return MethodResult.of(true, "Train disassembled");
        }
        return MethodResult.of(false, "Could not disassemble train");
    }

    /**
     * Returns the current station name.
     *
     * @return Name of station.
     */
    @LuaFunction
    public final String getStationName() {
        return station.getStation().name;
    }

    /**
     * Returns the current trains name.
     *
     * @return Name of train.
     */
    @LuaFunction
    public final String getTrainName() {
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
            AllPackets.channel.send(PacketDistributor.ALL.noArg(), new TrainEditReturnPacket(Train.id, name, Train.icon.getId()));
            return MethodResult.of(true, "Train name set to" + name);
        }
        //AllPackets.channel.sendToServer(new TrainEditPacket(train.id, name,train.icon.getId()));
        return MethodResult.of(false, "Train name cannot be blank");
    }

    /**
     * Gets the number of Bogeys attached to the current train.
     *
     * @return The number of Bogeys.
     */
    @LuaFunction
    public final int getBogeys() {
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
    public final boolean getPresentTrain() {
        return station.getStation().getPresentTrain() != null;
    }

    /**
     * Clears the schedule saved in the station.
     */
    @LuaFunction
    public final void clearSchedule() {
        schedule = null;
    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this == iPeripheral;
    }
}

