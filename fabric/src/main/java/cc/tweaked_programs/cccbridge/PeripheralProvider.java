package cc.tweaked_programs.cccbridge;

import cc.tweaked_programs.cccbridge.blockEntity.PeripheralBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PeripheralProvider implements IPeripheralProvider {

    @Nullable
    public IPeripheral getPeripheral(@NotNull Level world, @NotNull BlockPos pos, @NotNull Direction side) {
        BlockEntity block = world.getBlockEntity(pos);

        if (block instanceof PeripheralBlockEntity peripheralBlock)
            return peripheralBlock.getPeripheral(side);

        return null;
    }
}
