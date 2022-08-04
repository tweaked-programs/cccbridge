package cc.tweaked_programs.cccbridge.block.target;

import cc.tweaked_programs.cccbridge.Main;
import com.simibubi.create.content.logistics.block.display.DisplayLinkBlock;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class TargetBlockEntity extends BlockEntity implements IPeripheralTile {
    private TargetBlockPeripheral peripheral;

    public TargetBlockEntity(BlockPos pos, BlockState state) {
        super(Main.TARGET_BLOCK_ENTITY, pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new TargetBlockPeripheral(this);
        return peripheral;
    }

    public void updateContent(int offset, List<String> content) {
        int i=0;
        for (String line : content) {
            // Replace chars that exist in C and can't be displayed in CC:
            line = line.replaceAll("\u2588", "=");
            line = line.replaceAll("\u2592", "-");

            peripheral.repalceLine(offset + i, line);
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
