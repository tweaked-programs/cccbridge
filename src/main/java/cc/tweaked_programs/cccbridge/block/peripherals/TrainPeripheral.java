package cc.tweaked_programs.cccbridge.block.peripherals;

import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

public class TrainPeripheral implements IPeripheral {

    private final List<IComputerAccess> connectedComputers = new ArrayList<>();
    private final BlockPos pos;

    private final StationTileEntity Te;
    private final Level level;

    public TrainPeripheral(@NotNull BlockPos pos, Level level , BlockEntity te) {
        this.pos = pos;
        this.level = level;
        this.Te = (StationTileEntity) te;
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

    @LuaFunction
    public void assemble() {
        Te.assemble(Minecraft.getInstance().player.getUUID());
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

