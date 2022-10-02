package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.peripherals.SpeedControllerPeripheral;
import cc.tweaked_programs.cccbridge.peripherals.TrainPeripheral;
import com.simibubi.create.content.contraptions.relays.advanced.SpeedControllerTileEntity;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class PeripheralProvider implements IPeripheralProvider {

    @NotNull
    @Override
    public LazyOptional<IPeripheral> getPeripheral(@NotNull Level world, @NotNull BlockPos pos, @NotNull Direction side) {
        if (world.getBlockEntity(pos) instanceof StationTileEntity) {
            return LazyOptional.of(() -> new TrainPeripheral(pos, world));
        } else if (world.getBlockEntity(pos) instanceof SpeedControllerTileEntity) {
            return LazyOptional.of(() -> new SpeedControllerPeripheral(pos, world));
        }

        return LazyOptional.empty();
    }
}

