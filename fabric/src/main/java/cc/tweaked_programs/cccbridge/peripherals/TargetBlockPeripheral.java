package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * This peripheral is used by the Target Block. It is used to get data from Create Display Sources. The data has to be synced by the BlockEntity.
 *
 * @version 1.0
 */
public class TargetBlockPeripheral implements IPeripheral {
    private final TargetBlockEntity target_block_entity;
    private final Terminal term = new Terminal(32, 24);


    public TargetBlockPeripheral(TargetBlockEntity target_block_entity) {
        this.target_block_entity = target_block_entity;
    }

    public void replaceLine(int y, String line) {
        if (y < 0 || y >= term.getHeight())
            return;
        term.setCursorPos(0, y);
        term.clearLine();
        term.write(line);
    }

    public int getWidth() {
        return term.getWidth();
    }

    /**
     * Sets the new width of the display. Cannot be larger than 164 chars.
     *
     * @param width The new width of the display.
     * @throws LuaException When given number is not in range 1-164
     */
    @LuaFunction
    public final void setWidth(int width) throws LuaException {
        if (width < 1 || width > 164) throw new LuaException("Expected number in range 1-164");
        term.resize(width, 16);
    }

    public int getHeight() {
        return term.getHeight();
    }

    /**
     * Clears the whole screen.
     */
    @LuaFunction
    public final void clear() {
        term.clear();
    }

    /**
     * Clears the line at the cursor position.
     *
     * @param y The Y position of the to be cleared line.
     */
    @LuaFunction
    public final void clearLine(int y) {
        if (y < 1 || y > term.getHeight())
            return;
        term.setCursorPos(0, y - 1);
        term.clearLine();
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
     * Returns the current display size.
     *
     * @return Object[] {width, height}
     */
    @LuaFunction
    public final Object[] getSize() {
        return new Object[]{term.getWidth(), term.getHeight()};
    }

    @NotNull
    @Override
    public String getType() {
        return "create_target";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof TargetBlockPeripheral target && target.target_block_entity == target_block_entity;
    }
}