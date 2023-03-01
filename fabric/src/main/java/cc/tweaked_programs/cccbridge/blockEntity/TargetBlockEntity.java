package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.Misc;
import cc.tweaked_programs.cccbridge.peripherals.TargetBlockPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public class TargetBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.getBlockEntityType("target_block"), pos, state);
    }

    public void updateContent(int offset, List<String> content) {
        if (peripheral == null)
            return;
        int height = getHeight();
        int i = 0;
        String dot = Character.toString(183);
        for (String line : content) {
            if (i < height)
                peripheral.replaceLine(offset + i, Misc.CreateToComputersCharset(line));
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
