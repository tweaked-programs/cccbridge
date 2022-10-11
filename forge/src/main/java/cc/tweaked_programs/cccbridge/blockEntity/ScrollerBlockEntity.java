package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.CCCSoundEvents;
import cc.tweaked_programs.cccbridge.peripherals.ScrollerBlockPeripheral;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.phys.Vec3;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScrollerBlockEntity extends SmartTileEntity {
    private ScrollerBlockPeripheral peripheral;
    private boolean locked = false;
    private boolean updateLock = false;
    private boolean playTickSound = false;

    public ScrollerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.SCROLLER_BLOCK_ENTITY.get(), pos, state);
    }

    @Nullable
    public IPeripheral getPeripheral(Direction side) {
        if (side == this.getBlockState().getValue(BlockStateProperties.FACING).getOpposite()) {
            if (peripheral == null)
                peripheral = new ScrollerBlockPeripheral(this, this.getLevel());
            return peripheral;
        }
        return null;
    }

    public void setLock(boolean state) { locked = state; updateLock = true; }
    public void playTickSound() { playTickSound = true; }

    @Nullable
    public ScrollValueBehaviour getBehaviour() {
        TileEntityBehaviour behaviour = getBehaviour(ScrollValueBehaviour.TYPE);
        if (behaviour instanceof ScrollValueBehaviour scrollValueBehaviour)
            return scrollValueBehaviour;
        return null;
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

        scroller.tick();
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        ScrollValueBehaviour scroller = new ScrollValueBehaviour(this.getBlockState().getBlock().getName(), this, new ControllerValueBoxTransform())
                .between(-150, 150)
                .moveText(new Vec3(9, 0, 10))
                .withUnit(i -> new TranslatableComponent("cccbridge.general.unit.scroller"))
                .withCallback(i -> { if (this.peripheral != null) peripheral.newValue(i); })
                .onlyActiveWhen(() -> !(this.getLevel().getBlockState(this.getBlockPos()).getValue(BlockStateProperties.LOCKED)))
                .withStepFunction(context -> context.shift ? 1 : 10)
                .withFormatter(i -> {
                    StringBuilder number = new StringBuilder(String.valueOf(i));
                    if (i <= -10 || i >= 10)
                        number.insert(number.length()-1,'.');
                    else
                        number.insert( (i>=0) ? 0 : 1, "0.");
                    return number.toString();
                });
        scroller.value = 0;

        behaviours.add(scroller);
    }

    private static class ControllerValueBoxTransform extends ValueBoxTransform.Sided {
        @Override
        protected Vec3 getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 0.5);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) { return state.getValue(BlockStateProperties.FACING) == direction; }

        @Override
        protected float getScale() {
            return 0.5f;
        }
    }
}
