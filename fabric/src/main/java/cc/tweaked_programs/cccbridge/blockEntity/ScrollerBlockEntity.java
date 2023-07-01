package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.CCCRegister;
import cc.tweaked_programs.cccbridge.CCCSoundEvents;
import cc.tweaked_programs.cccbridge.behaviour.LuaScrollValueBehaviour;
import cc.tweaked_programs.cccbridge.peripherals.ScrollerBlockPeripheral;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.blockEntity.SmartBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.BlockEntityBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.utility.VecHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public class ScrollerBlockEntity extends SmartBlockEntity implements PeripheralBlockEntity {
    private ScrollerBlockPeripheral peripheral;
    private boolean locked = false;
    private boolean updateLock = false;
    private boolean playTickSound = false;
    private LuaScrollValueBehaviour scroller;

    public ScrollerBlockEntity(BlockPos pos, BlockState state) {
        super(CCCRegister.getBlockEntityType("scroller_block"), pos, state);
    }

    public IPeripheral getPeripheral(@Nullable Direction side) {
        if (side == this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite() || side == null) {
            if (peripheral == null)
                peripheral = new ScrollerBlockPeripheral(this);
            return peripheral;
        }
        return null;
    }

    public void setLock(boolean state) { locked = state; updateLock = true; }

    public int getValue() {
        return scroller.getValue();
    }

    public void setValue(int value) {
        scroller.setValue(value);
    }

    public void fireUpdateValueEvent() {
        if (peripheral != null)
            peripheral.sendEvent("scroller_changed", scroller.getValue());
    }

    public static void tick(Level world, BlockPos blockPos, BlockState state, BlockEntity be) {
        if (!(be instanceof ScrollerBlockEntity scroller))
            return;

        if (scroller.updateLock && state.getValue(BlockStateProperties.LOCKED) != scroller.locked) {
            world.playSound(
                    null,
                    blockPos,
                    scroller.locked ? CCCSoundEvents.CAGE_LOCK : CCCSoundEvents.CAGE_UNLOCK,
                    SoundSource.BLOCKS,
                    1.0f,
                    1.5f);
            world.setBlock(blockPos, state.setValue(BlockStateProperties.LOCKED, scroller.locked), 19); // 19 = BLOCK_UPDATE_FLAGS
            scroller.updateLock = false;
        }
        if (scroller.playTickSound) {
            world.playSound(
                    null,
                    blockPos,
                    AllSoundEvents.SCROLL_VALUE.getMainEvent(),
                    SoundSource.BLOCKS,
                    0.25f,
                    1.5f);
            scroller.playTickSound = false;
        }
    }

    @Override
    public void addBehaviours(List<BlockEntityBehaviour> behaviours) {
        if (scroller == null) {
            scroller = new LuaScrollValueBehaviour(this, new ControllerValueBoxTransform());
        }

        behaviours.add(scroller);
    }

    public int getLimit() {
        return scroller.getMax();
    }

    public void setLimit(int limit) {
        scroller.between(limit);
    }

    public boolean hasMinus() {
        return scroller.hasMinus();
    }

    public void toggleMinusSpectrum(boolean state) {
        scroller.setHasMinus(state);
    }

    private static class ControllerValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8D, 8D, 0.01D);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) { return state.getValue(BlockStateProperties.FACING) == direction; }

    }
}
