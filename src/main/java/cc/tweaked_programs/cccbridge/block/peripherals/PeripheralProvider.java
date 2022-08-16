package cc.tweaked_programs.cccbridge.block.peripherals;

import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import io.github.fabricators_of_create.porting_lib.util.LazyOptional;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PeripheralProvider implements IPeripheralProvider {

    public IPeripheral getPeripheral(@NotNull World world, @NotNull BlockPos pos, @NotNull Direction side) {
        if (world.getBlockEntity(pos) instanceof StationTileEntity) {
            return new TrainPeripheral(pos, world);
        }
        return null;
    }
}
