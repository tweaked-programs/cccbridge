package cc.tweaked_programs.cccbridge.common.computercraft.peripherals;

import cc.tweaked_programs.cccbridge.common.computercraft.TweakedPeripheral;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.ScrollerBlockEntity;
import dan200.computercraft.api.lua.LuaFunction;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

/**
 * This peripheral is used by the Scroller Pane. It allows to interact with its valued that the player can manipulate
 * (as long as this peripheral allows though, as it call lock it for outside interactions)
 *
 * @version 2.0
 */
public class ScrollerBlockPeripheral extends TweakedPeripheral<ScrollerBlockEntity> {
    public static double getVersion() {
        return 2.0D;
    }

    public ScrollerBlockPeripheral(ScrollerBlockEntity blockentity) {
        super("scroller", blockentity);
    }

    /**
     * Returns whether the Scroller Pane is locked or not.
     * @return The state
     */
    @LuaFunction
    public final boolean isLocked() {
        ScrollerBlockEntity be = getTarget();
        if (be != null) {
            Level level = getTarget().getLevel();

            if (level != null)
                return level.getBlockState(be.getBlockPos()).getValue(BlockStateProperties.LOCKED);
        }

        return false;
    }

    /**
     * Unlocks the Scroller Pane with state = false (default) or state = true so that players cannot continue to use it.
     * @param state Sate for lock
     */
    @LuaFunction
    public final void setLock(boolean state) {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            be.setLock(state);
    }

    /**
     * Returns the selected value of the Scroller Pane.
     *
     * @return The selected value.
     */
    @LuaFunction
    public final int getValue() {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            return be.getValue();

        return 0;
    }

    /**
     * Changes the selected value.
     * @param value The new value to select.
     */
    @LuaFunction
    public final void setValue(int value) {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            be.setValue(value);
    }

    /**
     * Returns the limit relative to zero.
     * E.g. 15 for from 0 to 15.
     *
     * @return The limit.
     */
    @LuaFunction
    public final int getLimit() {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            return be.getLimit();

        return 0;
    }

    /**
     * Returns whenever the Scroller Pane has the minus spectrum enabled.
     *
     * @return true for when the minus spectrum is enabled, and false for the opposite.
     */
    @LuaFunction
    public final boolean hasMinusSpectrum() {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            return be.hasMinus();

        return false;
    }

    /**
     * Enables or disables the minus spectrum.
     *
     * @param state true for enabled minus spectrum, false for the opposite.
     */
    @LuaFunction
    public final void toggleMinusSpectrum(boolean state) {
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            be.toggleMinusSpectrum(state);
    }

    /**
     * Sets a new limit relative to zero.
     * If minus spectrum enabled, the limit then gets mirrored to the minus spectrum too.
     *
     * @param limit The new limit.
     */
    @LuaFunction
    public final void setLimit(int limit) {
        limit = Mth.clamp(Math.abs(limit), 1, 200);
        ScrollerBlockEntity be = getTarget();
        if (be != null)
            be.setLimit(limit);
    }
}
