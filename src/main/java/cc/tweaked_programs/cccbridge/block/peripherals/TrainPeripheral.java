package cc.tweaked_programs.cccbridge.block.peripherals;

import com.mojang.authlib.GameProfile;
import com.simibubi.create.content.logistics.trains.entity.Train;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationEditPacket;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.TrainEditPacket;
import com.simibubi.create.foundation.networking.AllPackets;
import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.apis.ComputerAccess;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.core.computer.ComputerSystem;
import dan200.computercraft.core.tracking.ComputerTracker;
import net.minecraft.block.Block;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.core.jmx.Server;
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

    private Block get_block() {
        return this.level.getBlockEntity(pos).getCachedState().getBlock();
    }

    @LuaFunction
    public boolean assemble() {
        if (station.tryEnterAssemblyMode()) {
            station.assemble(UUID.fromString("069a79f4-44e9-4726-a5be-fca90e38aaf5"));
            //assemble stuff here
            return true;
        }
        return false;
    }

    @LuaFunction
    public boolean disassemble() {
        if (station.getStation().getPresentTrain().canDisassemble()) {
            Direction direction = station.getAssemblyDirection();
            BlockPos position = station.edgePoint.getGlobalPosition().up();
            ServerPlayerEntity player = new ServerPlayerEntity(level.getServer(), level.getServer().getOverworld(), new GameProfile(UUID.fromString("f3fa25b0-795f-4abb-a08b-53ad30c6f59e"), "MegaLuke1505"));
            station.getStation().getPresentTrain().disassemble(player,direction, position);
            return true;
        }
        return false;
    }

    @LuaFunction
    public String getStationName() {
        return station.getStation().name;
    }

    @LuaFunction
    public boolean isAssembled() {
        if (station.getStation().getPresentTrain() == null) {
            return false;
        } else return station.getStation().getPresentTrain().canDisassemble();
    }

    @LuaFunction
    public String getTrainName() {
        return station.getStation().getPresentTrain().name.asString();
    }

    @LuaFunction
    public boolean setStationName(@NotNull String name) {
        AllPackets.channel.sendToServer(StationEditPacket.configure(station.getPos(), false, name));
        return true;
    }

    @LuaFunction
    public boolean setTrainName(@NotNull String name) {
        if (station.getStation().getPresentTrain() == null) {
            return false;
        }
        Train train = station.getStation().getPresentTrain();
        AllPackets.channel.sendToServer(new TrainEditPacket(train.id, name, train.icon.getId()));
        return true;
    }
    /**
     * Will be called when a computer connects to our block
     */
    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        connectedComputers.add(computer);

    }

    @Override
    public boolean equals(@Nullable IPeripheral iPeripheral) {
        return this == iPeripheral;
    }
}

