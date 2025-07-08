package dev.kleinbox.cccbridge.common.minecraft.blockEntity;

import dan200.computercraft.api.peripheral.IPeripheral;
import dev.kleinbox.cccbridge.common.CCCRegistries;
import dev.kleinbox.cccbridge.common.computercraft.peripherals.SourceBlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class SourceBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private SourceBlockPeripheral peripheral;

    public SourceBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<SourceBlockEntity>) CCCRegistries.SOURCE_BLOCK_ENTITY.get(), pos, state);
    }

    public IPeripheral getPeripheral(@Nullable Direction side) {
        if (peripheral == null)
            peripheral = new SourceBlockPeripheral(this);
        return peripheral;
    }

    public void setSize(int width, int height) {
        if (peripheral != null)
            peripheral.setSize(width, height);
    }
}
