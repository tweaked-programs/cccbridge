package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.peripherals.ScrollerBlockPeripheral;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import com.simibubi.create.foundation.tileEntity.SmartTileEntity;
import com.simibubi.create.foundation.tileEntity.TileEntityBehaviour;
import com.simibubi.create.foundation.tileEntity.behaviour.ValueBoxTransform;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.VecHelper;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Properties;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class ScrollerBlockEntity extends SmartTileEntity implements IPeripheralTile {
    private ScrollerBlockPeripheral peripheral;

    public ScrollerBlockEntity(BlockPos pos, BlockState state) {
        super(BlockRegister.getBlockEntityType("source_block"), pos, state);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new ScrollerBlockPeripheral(this);
        return peripheral;
    }

    @Nullable
    public ScrollValueBehaviour getBehaviour() {
        TileEntityBehaviour behaviour = getBehaviour(ScrollValueBehaviour.TYPE);
        if (behaviour instanceof ScrollValueBehaviour scrollValueBehaviour)
            return scrollValueBehaviour;
        return null;
    }

    @Override
    public void addBehaviours(List<TileEntityBehaviour> behaviours) {
        ScrollValueBehaviour targetSpeed = new ScrollValueBehaviour(getCachedState().getBlock().getName(), this, new ControllerValueBoxTransform())
                .between(-160, 160)
                .moveText(new Vec3d(9, 0, 10))
                .withFormatter(i -> {
                    StringBuilder number = new StringBuilder(String.valueOf(i));
                    if (i <= -10 || i >= 10)
                        number.insert(number.length()-1,'.').toString();
                    else {
                        if (i >= 0)
                            number.insert(0, "0.").toString();
                        else
                            number.insert(number.length(), ".0").toString();
                    }
                    return number.toString();
                })
                .withUnit(i -> new TranslatableText("cccbridge.general.unit.scroller"))
                .withCallback(i -> {
                    peripheral.newValue();
                })
                .withStepFunction(ScrollerBlockEntity::step);
        targetSpeed.value = 0;

        behaviours.add(targetSpeed);

        registerAwardables(behaviours, AllAdvancements.SPEED_CONTROLLER);
    }

    public static int step(ScrollValueBehaviour.StepContext context) {
        return context.shift ? 1 : 10;
    }

    private class ControllerValueBoxTransform extends ValueBoxTransform.Sided {

        @Override
        protected Vec3d getSouthLocation() {
            return VecHelper.voxelSpace(8, 8, 1);
        }

        @Override
        protected boolean isSideActive(BlockState state, Direction direction) {
            return state.get(Properties.FACING) == direction;
        }

        @Override
        protected float getScale() {
            return 0.5f;
        }
    }
}
