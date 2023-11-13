package cc.tweaked_programs.cccbridge.common.computercraft;

import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.PeripheralBlockEntity;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

public class TweakedPeripheralProvider implements IPeripheralProvider {
    @Override
    public LazyOptional<IPeripheral> getPeripheral(Level level, BlockPos pos, Direction dir) {
        if (level.getBlockEntity(pos) instanceof PeripheralBlockEntity peripheralBlockEntity) {
            IPeripheral peripheral = peripheralBlockEntity.getPeripheral(dir);
            if (peripheral != null)
                return LazyOptional.of(() -> peripheral);
        }
        return LazyOptional.empty();
    }
}
