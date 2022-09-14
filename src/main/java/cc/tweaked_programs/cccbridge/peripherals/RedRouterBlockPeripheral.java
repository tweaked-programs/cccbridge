package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.computer.ComputerSide;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedRouterBlockPeripheral implements IPeripheral {
    private final RedRouterBlockEntity redrouter_be;

    public RedRouterBlockPeripheral(RedRouterBlockEntity redrouter_block_entity) {
        this.redrouter_be = redrouter_block_entity;
    }

    @NotNull
    @Override
    public String getType() {
        return "redrouter";
    }

    public Direction getActualSide(ComputerSide side) {
        Direction facing = redrouter_be.getFacing();
        return switch (side.getName()) {
            case ("front") -> facing.getOpposite();
            case ("back") -> facing;
            case ("left") -> facing.rotateYClockwise();
            case ("right") -> facing.rotateYCounterclockwise();
            case ("top") -> Direction.DOWN;
            case ("bottom") -> Direction.UP;
            default -> Direction.NORTH;
        };
    }

    /**
     * Toggles a redstone signal for a specific side.
     *
     * @param side The side to set.
     * @param on   The signals state true(15) / false(0).
     */
    @LuaFunction
    public final void setOutput(ComputerSide side, boolean on) {
        redrouter_be.setPower(getActualSide(side).getName(), on ? 15 : 0);
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
        return redrouter_be.getPower(getActualSide(side)) > 0;
    }

    /**
     * Get the current redstone input of a specific side.
     *
     * @param side The side to get.
     * @return Whether the redstone input is on or off.
     */
    @LuaFunction
    public final boolean getInput(ComputerSide side) {
        return redrouter_be.getRedstoneInput(getActualSide(side)) > 0;
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
        redrouter_be.setPower(getActualSide(side).getName(), value);
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
        return redrouter_be.getPower(getActualSide(side));
    }

    /**
     * Get the redstone input signal strength for a specific side.
     *
     * @param side The side to get.
     * @return The input signal strength, between 0 and 15.
     */
    @LuaFunction({"getAnalogInput", "getAnalogueInput"})
    public final int getAnalogInput(ComputerSide side) {
        return redrouter_be.getRedstoneInput(getActualSide(side));
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof RedRouterBlockPeripheral redrouter && redrouter.redrouter_be == redrouter_be;
    }
}
