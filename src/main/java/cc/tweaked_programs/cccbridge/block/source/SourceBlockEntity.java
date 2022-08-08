package cc.tweaked_programs.cccbridge.block.source;

import cc.tweaked_programs.cccbridge.Main;

import dan200.computercraft.api.peripheral.IPeripheral;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SourceBlockEntity extends BlockEntity {
    private SourceBlockPeripheral peripheral;

    public SourceBlockEntity(BlockPos pos, BlockState state) {
        super(Main.SOURCE_BLOCK_ENTITY.get(), pos, state);
    }

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
