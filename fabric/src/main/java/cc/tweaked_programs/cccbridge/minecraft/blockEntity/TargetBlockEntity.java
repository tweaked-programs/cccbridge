package cc.tweaked_programs.cccbridge.minecraft.blockEntity;

import cc.tweaked_programs.cccbridge.assistance.PeripheralBlockEntity;
import cc.tweaked_programs.cccbridge.computercraft.peripherals.TargetBlockPeripheral;
import cc.tweaked_programs.cccbridge.Registries;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TargetBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<TargetBlockEntity>) Registries.TARGET_BLOCK_ENTITY.get(), pos, state);
    }

    public void updateContent(int offset, List<String> content) {
        if (peripheral == null)
            return;
        int height = getHeight();
        int i = 0;
        for (String line : content) {
            if (i < height)
                peripheral.replaceLine(offset + i, line);
            i++;
        }
    }

    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new TargetBlockPeripheral(this);
        return peripheral;
    }

    public int getWidth() {
        if (peripheral == null)
            return 1;
        return peripheral.getWidth();
    }

    public int getHeight() {
        if (peripheral == null)
            return 1;
        return peripheral.getHeight();
    }
}
