package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import cc.tweaked_programs.cccbridge.peripherals.RedRouterBlockPeripheral;
import cc.tweaked_programs.cccbridge.peripherals.SourceBlockPeripheral;
import cc.tweaked_programs.cccbridge.peripherals.TargetBlockPeripheral;
import cc.tweaked_programs.cccbridge.peripherals.TrainPeripheral;
import com.simibubi.create.content.logistics.trains.management.edgePoint.station.StationTileEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

public class PeripheralProvider implements IPeripheralProvider {

    public IPeripheral getPeripheral(@NotNull World world, @NotNull BlockPos pos, @NotNull Direction side) {
        BlockEntity block = world.getBlockEntity(pos);

        if (block instanceof StationTileEntity) {
            // Train Station
            return new TrainPeripheral(pos, world);
        } else if (block instanceof RedRouterBlockEntity) {
            // RedRouter Block
            return new RedRouterBlockPeripheral((RedRouterBlockEntity) block);
        } else if (block instanceof SourceBlockEntity) {
            // Source Block
            return new SourceBlockPeripheral((SourceBlockEntity) block);
        } else if (block instanceof TargetBlockEntity) {
            // Target Block
            return new TargetBlockPeripheral((TargetBlockEntity) block);
        }

        return null;
    }
}
