package cc.tweaked_programs.cccbridge.block.peripherals;

import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    private final Level level;
    private final StationTileEntity station;

    public TrainPeripheral(@NotNull BlockPos pos, Level level) {
        this.pos = pos;
        this.level = level;
        this.station = (StationTileEntity) level.getBlockEntity(pos);
    }


    @NotNull
    @Override
    public String getType() {
        return "Train_Station";
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        connectedComputers.remove(computer);
    }

    private Block get_block() {
        return this.level.getBlockEntity(pos).getBlockState().getBlock();
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
        if (station.getStation().getNearestTrain().canDisassemble()) {
            Direction direction = station.getAssemblyDirection();
            BlockPos position = station.edgePoint.getGlobalPosition().above();
            station.getStation().getPresentTrain().disassemble(direction, position);
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
        if (station.getStation().getNearestTrain() == null) {
            return false;
        }else return station.getStation().getNearestTrain().canDisassemble();
    }

    @LuaFunction
    public String getTrainName() {
        return Objects.requireNonNull(station.getStation().getImminentTrain()).name.getContents();
    }

    @LuaFunction
    public boolean setStationName(@NotNull String name) {
        station.getStation().name = name;
        return true;
    }

    @LuaFunction
    public boolean setTrainName(@NotNull String name) {
        if (station.getStation().getNearestTrain() == null) {
            return false;
        }
        station.getStation().getImminentTrain().name = new Component() {
            @Override
            public Style getStyle() {
                return null;
            }

            @Override
            public String getContents() {
                return name;
            }

            @Override
            public List<Component> getSiblings() {
                return null;
            }

            @Override
            public MutableComponent plainCopy() {
                return null;
            }

            @Override
            public MutableComponent copy() {
                return null;
            }

            @Override
            public FormattedCharSequence getVisualOrderText() {
                return null;
            }
        };
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

