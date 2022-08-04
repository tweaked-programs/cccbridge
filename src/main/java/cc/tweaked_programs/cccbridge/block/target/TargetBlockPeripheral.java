package cc.tweaked_programs.cccbridge.block.target;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class TargetBlockPeripheral implements IPeripheral {
    private final TargetBlockEntity target_block_entity;
    private Terminal term = new Terminal(32,16);


    TargetBlockPeripheral(TargetBlockEntity target_block_entity) { this.target_block_entity = target_block_entity; }

    public void repalceLine(int y, String line) {
        if (y < 0 || y >= term.getHeight())
            return;
        term.setCursorPos(0,y);
        term.write(line);
    }

    public int getWidth() {
        return term.getWidth();
    }

    public int getHeight() {
        return term.getHeight();
    }


    @LuaFunction
    public final void clear() {
        term.clear();
    }

    @LuaFunction
    public final void clearLine(int y) {
        if (y < 1 || y > term.getHeight())
            return;
        term.setCursorPos(0,y-1);
        term.clearLine();
    }

    @LuaFunction
    public final Object getLine(int y) {
        if (y < 1 || y > term.getHeight())
            return null;

        TextBuffer line = term.getLine(y-1);
        return line.toString();
    }

    @LuaFunction
    public final Object[] getSize() {
        return new Object[] { term.getWidth(), term.getHeight() };
    }

    @LuaFunction
    public final Object[] setWidth(int width) {
        if (width > 164)
            return new Object[] {false, "Given size is larger than 164!"};
        else if (width < 1)
            return new Object[] {false, "Given size cannot be lower than 1 duh"};

        term.resize(width,16);
        return new Object[] {true};
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
