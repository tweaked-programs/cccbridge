package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.peripherals.TargetBlockPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class TargetBlockEntity extends BlockEntity implements IPeripheralTile {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.getBlockEntityType("target_block"), pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new TargetBlockPeripheral(this);
        return peripheral;
    }

    public void updateContent(int offset, List<String> content) {
        if (peripheral == null)
            return;
        int height = getHeight();
        int i = 0;
        String dot = Character.toString(183);
        for (String line : content) {
            if (i < height) {
                // Replace chars that exist in C and can't be displayed in CC:
                line = line.replaceAll("\u2588", "=");
                line = line.replaceAll("\u2592", "-");
                line = line.replaceAll("\u2591", dot);

                peripheral.replaceLine(offset + i, line);
            }
            i++;
        }
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
