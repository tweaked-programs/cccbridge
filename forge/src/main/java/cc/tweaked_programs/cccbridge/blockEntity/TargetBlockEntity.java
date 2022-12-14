package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.peripherals.TargetBlockPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TargetBlockEntity extends BlockEntity {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.TARGET_BLOCK_ENTITY.get(), pos, state);
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

    public IPeripheral getPeripheral(Direction side) {
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
