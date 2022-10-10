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
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ScrollerBlockEntity extends SmartTileEntity implements IPeripheralTile {
    private ScrollerBlockPeripheral peripheral;
    private boolean locked = false;
    private boolean updateLock = false;
    private boolean playTickSound = false;

    public ScrollerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.getBlockEntityType("scroller_block"), pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (side == this.getCachedState().get(Properties.FACING).getOpposite()) {
            if (peripheral == null)
                peripheral = new ScrollerBlockPeripheral(this, this.getWorld());
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

    public static void tick(World world, BlockPos blockPos, BlockState state, BlockEntity be) {
        if (!(be instanceof ScrollerBlockEntity scroller))
            return;

        if (scroller.updateLock && state.get(Properties.LOCKED) != scroller.locked) {
            world.playSound(
                    null,
                    blockPos,
                    scroller.locked ? CCCSoundEvents.CAGE_LOCK : CCCSoundEvents.CAGE_UNLOCK,
                    SoundCategory.BLOCKS,
                    1.0f,
                    1.5f);
            world.setBlockState(blockPos, state.with(Properties.LOCKED, scroller.locked));
            scroller.updateLock = false;
        }
        if (scroller.playTickSound) {
            world.playSound(
                    null,
                    blockPos,
                    AllSoundEvents.SCROLL_VALUE.getMainEvent(),
                    SoundCategory.BLOCKS,
                    0.25f,
                    1.5f);
            scroller.playTickSound = false;
        }

        scroller.tick();
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        ScrollValueBehaviour scroller = new ScrollValueBehaviour(getCachedState().getBlock().getName(), this, new ControllerValueBoxTransform())
                .between(-150, 150)
                .moveText(new Vec3d(9, 0, 10))
                .withUnit(i -> new TranslatableText("cccbridge.general.unit.scroller"))
                .withCallback(i -> { if (this.peripheral != null) peripheral.newValue(i); })
                .interactiveWhen(playerEntity -> !(playerEntity.getWorld().getBlockState(this.getPos()).get(Properties.LOCKED)))
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
        protected Vec3d getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 0);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) { return state.get(Properties.FACING) == direction; }

        @Override
        protected float getScale() {
            return 0.5f;
        }
    }
}
