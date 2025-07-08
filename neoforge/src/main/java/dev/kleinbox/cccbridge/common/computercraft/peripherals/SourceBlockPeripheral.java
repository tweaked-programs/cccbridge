package dev.kleinbox.cccbridge.common.computercraft.peripherals;

import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.core.apis.TermMethods;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import dev.kleinbox.cccbridge.common.computercraft.TweakedPeripheral;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.SourceBlockEntity;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

/**
 * This peripheral is used by the Source Block. It is used to give some kind of Create Display Targets data.
 * The peripheral acts similar to a normal Terminal, with some implementations from the Window API and other limitations like no control over the colors.
 *
 * @version 1.1
 */
public class SourceBlockPeripheral extends TermMethods implements TweakedPeripheral<SourceBlockEntity> {
    private final SourceBlockEntity be;

    public static double getVersion() {
        return 1.1D;
    }

    private final Terminal term = new Terminal(4, 2, false);

    public SourceBlockPeripheral(SourceBlockEntity blockentity) {
        be = blockentity;
    }

    public void setSize(int width, int height) {
        int oldW = term.getWidth();
        int oldH = term.getHeight();

        if (oldW == width && oldH == height)
            return;

        term.resize(width, height);
        sendEvent("monitor_resize");
    }

    public List<String> getContent() {
        List<String> content = new LinkedList<>();

        for (int i = 0; i < term.getHeight(); i++)
            content.add( term.getLine(i).toString());

        return content;
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

    @Override
    public Terminal getTerminal() throws LuaException {
        return term;
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

    @Override
    public @NotNull String getType() {
        return "create_source";
    }

    @Override
    public @Nullable SourceBlockEntity getTarget() {
        return be;
    }
}