package cc.tweaked_programs.cccbridge.block.source;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.core.terminal.Terminal;
import dan200.computercraft.core.terminal.TextBuffer;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

public class SourceBlockPeripheral implements IPeripheral {
    private final SourceBlockEntity source_block_entity;
    private Terminal term = new Terminal(4,2);

    SourceBlockPeripheral(SourceBlockEntity source_block_entity) { this.source_block_entity = source_block_entity; }

    public void setSize(int width, int height) {
        term.resize(width,height);
    }

    public List<String> getContent() {
        List<String> content = new LinkedList<>();

        // Replace chars that exist in CC and can't be displayed in C:
        for (int i=0;i<term.getHeight(); i++) {
            String line = term.getLine(i).toString();
            for (int j=0; j<line.length(); j++) {
                char ch = line.charAt(j);

                // Printable chars in byte range
                if (ch < 32 || (ch > 126 && ch < 161))
                    line = line.substring(0, j) + '?' + line.substring(j+1);
            }
            content.add(line);
        }

        return content;
    }


    @LuaFunction
    public final void setCursorPos(int x, int y) {
        term.setCursorPos(x-1,y-1);
    }

    @LuaFunction
    public final void write(String text) {
        term.write(text);
        term.setCursorPos(term.getCursorX()+text.length(), term.getCursorY());
    }

    @LuaFunction
    public final void scroll(int yDiff) {
        term.scroll(yDiff);
    }

    @LuaFunction
    public final void clear() {
        term.clear();
    }

    @LuaFunction
    public final void clearLine() {
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
    public final Object[] getCursorPos() {
        return new Object[] { term.getCursorX()+1, term.getCursorY()+1 };
    }

    @LuaFunction
    public final Object[] getSize() {
        return new Object[] { term.getWidth(), term.getHeight() };
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