package cc.tweaked_programs.cccbridge.block.source;

import cc.tweaked_programs.cccbridge.CCCBridge;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SourceBlockEntity extends BlockEntity implements IPeripheralTile {
    private SourceBlockPeripheral peripheral;

    public SourceBlockEntity(BlockPos pos, BlockState state) {
        super(CCCBridge.SOURCE_BLOCK_ENTITY, pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
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
