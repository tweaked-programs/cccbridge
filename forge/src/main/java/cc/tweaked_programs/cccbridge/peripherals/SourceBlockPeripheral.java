package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

/**
 * This peripheral is used by the Source Block. It is used to give some kind of Create Display Targets data.
 * The peripheral acts similar to a normal Terminal, with some implementations from the Window API and other limitations like no control over the colors.
 *
 * @version 1.0
 */
public class SourceBlockPeripheral implements IPeripheral {
    private final SourceBlockEntity source_block_entity;
    private final Terminal term = new Terminal(4, 2, false);
    private final List<IComputerAccess> pcs = new LinkedList<>();

    public SourceBlockPeripheral(SourceBlockEntity source_block_entity) {
        this.source_block_entity = source_block_entity;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        pcs.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        pcs.removeIf(p -> (p.getID() == computer.getID()));
    }

    public void resize() {
        for (IComputerAccess pc : pcs)
            pc.queueEvent("monitor_resize", pc.getAttachmentName());
    }

    public void setSize(int width, int height) {
        int oldW = term.getWidth();
        int oldH = term.getHeight();
        if (!(oldW != width || oldH != height)) return;
        term.resize(width, height);
        resize();
    }

    public List<String> getContent() {
        List<String> content = new LinkedList<>();

        // Replace chars that exist in CC and can't be displayed in C:
        for (int i = 0; i < term.getHeight(); i++) {
            String line = term.getLine(i).toString();
            for (int j = 0; j < line.length(); j++) {
                char ch = line.charAt(j);

                // Printable chars in byte range
                if (ch < 32 || (ch > 126 && ch < 161))
                    line = line.substring(0, j) + '?' + line.substring(j + 1);
            }
            content.add(line);
        }

        return content;
    }


    /**
     * Sets the position of the cursor. term.write will begin from this position.
     *
     * @param x The new x position of the cursor.
     * @param y The new y position of the cursor.
     */
    @LuaFunction
    public final void setCursorPos(int x, int y) {
        term.setCursorPos(x - 1, y - 1);
    }

    /**
     * Will write the given input to the linked display.
     *
     * @param text The string to be written.
     */
    @LuaFunction
    public final void write(String text) {
        term.write(text);
        term.setCursorPos(term.getCursorX() + text.length(), term.getCursorY());
    }

    /**
     * Scrolls the displays content vertically for [yDiff] lines.
     *
     * @param yDiff How many lines will the display scroll.
     */
    @LuaFunction
    public final void scroll(int yDiff) {
        term.scroll(yDiff);
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
     */
    @LuaFunction
    public final void clearLine() {
        term.clearLine();
    }

    /**
     * Returns the line at the wanted display position.
     *
     * @param y the y position on the display.
     * @return The string at wanted position.
     * @throws LuaException When given number is not in range 1-[terminal height]
     */
    @LuaFunction
    public final String getLine(int y) throws LuaException {
        if (y < 1 || y > term.getHeight()) throw new LuaException("Expected number in range 1-" + term.getHeight());

        TextBuffer line = term.getLine(y - 1);
        return line.toString();
    }

    /**
     * Returns the current current cursor position.
     *
     * @return Object[] {posX, posY}
     */
    @LuaFunction
    public final Object[] getCursorPos() {
        return new Object[]{term.getCursorX() + 1, term.getCursorY() + 1};
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
        return "create_source";
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof SourceBlockPeripheral source && source.source_block_entity == source_block_entity;
    }
}