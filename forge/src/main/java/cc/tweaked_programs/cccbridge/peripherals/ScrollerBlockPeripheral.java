package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ScrollerBlockPeripheral implements IPeripheral {
    private final ScrollerBlockEntity scroller;
    private final Level level;
    private final List<IComputerAccess> pcs = new LinkedList<>();

    public ScrollerBlockPeripheral(ScrollerBlockEntity block_entity, Level level) {
        this.scroller = block_entity;
        this.level = level;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        pcs.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        pcs.removeIf(p -> (p.getID() == computer.getID()));
    }

    public void newValue(int value) {
        for (IComputerAccess pc : pcs)
            pc.queueEvent("scroller_changed", pc.getAttachmentName(), (double)(value/10));
    }


    /**
     * Returns whether the Scroller Pane is locked or not.
     * @return The state
     */
    @LuaFunction
    public final boolean isLocked() {
        return level.getBlockState(scroller.getBlockPos()).getValue(BlockStateProperties.LOCKED);
    }

    /**
     * Unlocks the Scroller Pane with state = false (default) or state = true so that players cannot continue to use it.
     * @param state Sate for lock
     */
    @LuaFunction
    public final void setLock(boolean state) { scroller.setLock(state); }

    /**
     * Returns the current value of the Scroller Pane.
     * @return Value (-15.0 - 15.0)
     */
    @LuaFunction
    public final double getValue() {
        ScrollValueBehaviour scrollValueBehaviour = scroller.getBehaviour();
        if (scrollValueBehaviour == null) return 0.0f;

        return (double)scrollValueBehaviour.getValue()/10;
    }

    /**
     * Sets a new value for the Scroller Pane
     * @param value The new value (Must be in between -15.0 - 15.0)
     */
    @LuaFunction
    public final void setValue(double value) {
        ScrollValueBehaviour scrollValueBehaviour = scroller.getBehaviour();
        if (scrollValueBehaviour == null) return;

        if (value > 15) value = 15;
        else if (value < -15) value = -15;

        scrollValueBehaviour.setValue((int)(value*10));
        scroller.playTickSound();
    }


    @NotNull
    @Override
    public String getType() {
        return "scroller";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof ScrollerBlockPeripheral s && s.scroller == scroller;
    }
}
