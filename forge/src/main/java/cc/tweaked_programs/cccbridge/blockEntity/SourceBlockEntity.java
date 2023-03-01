package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.peripherals.SourceBlockPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SourceBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private SourceBlockPeripheral peripheral;

    public SourceBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SOURCE_BLOCK_ENTITY.get(), pos, state);
    }

    public IPeripheral getPeripheral(Direction side) {
        if (peripheral == null)
            peripheral = new SourceBlockPeripheral(this);
        return peripheral;
    }

    @Nullable
    public List<String> getContent() {
        if (peripheral == null)
            return null;
        return peripheral.getContent();
    }

    public void setSize(int width, int height) {
        if (peripheral != null)
            peripheral.setSize(width, height);
    }
}
