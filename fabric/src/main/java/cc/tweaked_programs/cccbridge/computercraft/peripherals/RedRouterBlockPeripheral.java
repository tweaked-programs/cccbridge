package cc.tweaked_programs.cccbridge.computercraft.peripherals;

import cc.tweaked_programs.cccbridge.assistance.TweakedPeripheral;
import cc.tweaked_programs.cccbridge.minecraft.blockEntity.RedRouterBlockEntity;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.core.Direction;

import java.util.LinkedList;
import java.util.List;

/**
 * This peripheral is used by the RedRouter. It works very similar to the Redstone API.
 *
 * @version 1.0
 */
public class RedRouterBlockPeripheral extends TweakedPeripheral<RedRouterBlockEntity> {
    public static double getVersion() {
        return 1.0D;
    }

    public static final String REDSTONE_EVENT = "redstone";
    private final List<IComputerAccess> pcs = new LinkedList<>();

    public RedRouterBlockPeripheral(RedRouterBlockEntity blockentity) {
        super("redrouter", blockentity);
    }

    public Direction getActualSide(ComputerSide side) {
        RedRouterBlockEntity be = getTarget();

        if (be != null) {
            Direction facing = getTarget().getFacing();
            return switch (side.getName()) {
                case ("front") -> facing.getOpposite();
                case ("back") -> facing;
                case ("left") -> facing.getClockWise();
                case ("right") -> facing.getCounterClockWise();
                case ("top") -> Direction.DOWN;
                case ("bottom") -> Direction.UP;
                default -> Direction.NORTH;
            };
        }

        return Direction.NORTH;
    }

    /**
     * Toggles a redstone signal for a specific side.
     *
     * @param side The side to set.
     * @param on   The signals state true(15) / false(0).
     */
    @LuaFunction
    public final void setOutput(ComputerSide side, boolean on) {
        RedRouterBlockEntity be = getTarget();
        if (be != null)
            be.setPower(getActualSide(side).getName(), on ? 15 : 0);
    }

    /**
     * Get the current redstone output of a specific side.
     *
     * @param side The side to get.
     * @return Whether the redstone output is on or off.
     * @see #setOutput
     */
    @LuaFunction
    public final boolean getOutput(ComputerSide side) {
        RedRouterBlockEntity be = getTarget();
        if (be != null)
            return be.getPower(getActualSide(side)) > 0;

        return false;
    }

    /**
     * Get the current redstone input of a specific side.
     *
     * @param side The side to get.
     * @return Whether the redstone input is on or off.
     */
    @LuaFunction
    public final boolean getInput(ComputerSide side) {
        RedRouterBlockEntity be = getTarget();
        if (be != null)
            return be.getRedstoneInput(getActualSide(side)) > 0;

        return false;
    }

    /**
     * Set a redstone signal strength for a specific side.
     *
     * @param side  The side to set.
     * @param value The signal strength between 0 and 15.
     * @throws LuaException If {@code value} is not betwene 0 and 15.
     */
    @LuaFunction({"setAnalogOutput", "setAnalogueOutput"})
    public final void setAnalogOutput(ComputerSide side, int value) throws LuaException {
        if (value < 0 || value > 15) throw new LuaException("Expected number in range 0-15");
        RedRouterBlockEntity be = getTarget();

        if (be != null)
            be.setPower(getActualSide(side).getName(), value);
    }

    /**
     * Get the redstone output signal strength for a specific side.
     *
     * @param side The side to get.
     * @return The output signal strength, between 0 and 15.
     * @see #setAnalogOutput
     */
    @LuaFunction({"getAnalogOutput", "getAnalogueOutput"})
    public final int getAnalogOutput(ComputerSide side) {
        RedRouterBlockEntity be = getTarget();
        if (be != null)
            return be.getPower(getActualSide(side));

        return 0;
    }

    /**
     * Get the redstone input signal strength for a specific side.
     *
     * @param side The side to get.
     * @return The input signal strength, between 0 and 15.
     */
    @LuaFunction({"getAnalogInput", "getAnalogueInput"})
    public final int getAnalogInput(ComputerSide side) {
        RedRouterBlockEntity be = getTarget();
        if (be != null)
            return be.getRedstoneInput(getActualSide(side));

        return 0;
    }
}
