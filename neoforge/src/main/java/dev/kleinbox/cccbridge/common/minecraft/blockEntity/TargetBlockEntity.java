package dev.kleinbox.cccbridge.common.minecraft.blockEntity;

import dev.kleinbox.cccbridge.common.CCCRegistries;
import dev.kleinbox.cccbridge.common.computercraft.peripherals.TargetBlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<TargetBlockEntity>) CCCRegistries.TARGET_BLOCK_ENTITY.get(), pos, state);
    }

    public @NotNull TargetBlockPeripheral getPeripheral(@Nullable Direction side) {
        if (peripheral == null)
            peripheral = new TargetBlockPeripheral(this);
        return peripheral;
    }
}
