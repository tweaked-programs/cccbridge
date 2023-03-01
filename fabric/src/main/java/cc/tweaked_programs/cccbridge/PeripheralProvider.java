package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntity.PeripheralBlockEntity;
import cc.tweaked_programs.cccbridge.peripherals.TrainPeripheral;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

public class PeripheralProvider implements IPeripheralProvider {

    @NotNull
    public IPeripheral getPeripheral(@NotNull Level world, @NotNull BlockPos pos, @NotNull Direction side) {
        BlockEntity block = world.getBlockEntity(pos);

        if (block instanceof StationTileEntity)
            return new TrainPeripheral(pos, world);

        else if (block instanceof PeripheralBlockEntity peripheralBlock)
            return peripheralBlock.getPeripheral(side);

        return null;
    }
}
