package dev.kleinbox.cccbridge.common.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import dev.kleinbox.cccbridge.common.computercraft.TweakedPeripheral;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.TargetBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This peripheral is used by the Target Block. It is used to get data from Create Display Sources. The data has to be synced by the BlockEntity.
 *
 * @version 1.2
 */
public class TargetBlockPeripheral implements TweakedPeripheral<TargetBlockEntity> {
    private final TargetBlockEntity be;

    public static double getVersion() {
        return 1.2D;
    }

    public final Terminal term = new Terminal(32, 8, true);

    public TargetBlockPeripheral(TargetBlockEntity be) {
        this.be = be;
    }

    /**
     * Resizes the terminal.
     *
     * @param width The new width of the terminal.
     * @param height The new height of the terminal.
     * @throws LuaException Whenever the given numbers are smaller than 1.
     */
    @LuaFunction
    public final void resize(int width, int height) throws LuaException {
        if (width < 1 || height < 1)
            throw new LuaException("The width and height of the terminal must be bigger than zero.");

        term.resize(width, height);
    }

    /**
     * Returns the line at the wanted display position.
     *
     * @param y The y position on the display.
     * @return The string from the given Y position.
     * @throws LuaException When given number is not in range 1-[terminal height]
     */
    @LuaFunction
    public final String getLine(int y) throws LuaException {
        if (y < 1 || y > term.getHeight()) throw new LuaException("Expected number in range 1-" + term.getHeight());

        TextBuffer line = term.getLine(y - 1);
        return line.toString();
    }

    /**
     * Dumps a copy of the current content.
     * @return A table with all lines of the target block.
     */
    @LuaFunction
    public final String[] dump() {
        int height = term.getHeight();
        String[] dump = new String[height];

        for (int i=0; i<height; i++)
            dump[i] = term.getLine(i).toString();

        return dump;
    }

    /**
     * Returns the current display size.
     *
     * @return Object[] {width, height}
     */
    @LuaFunction
    public final Object[] getSize() {
        return new Object[]{term.getWidth(), term.getHeight()};
    }

    @Override
    public @NotNull String getType() {
        return "create_target";
    }

    @Override
    public @Nullable TargetBlockEntity getTarget() {
        return be;
    }
}