package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import com.simibubi.create.foundation.tileEntity.behaviour.scrollvalue.ScrollValueBehaviour;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IComputerAccess;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.text.LiteralText;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class ScrollerBlockPeripheral implements IPeripheral {
    private final ScrollerBlockEntity scroller;
    private final List<IComputerAccess> pcs = new LinkedList<>();
    private boolean togglestate = true;

    public ScrollerBlockPeripheral(ScrollerBlockEntity block_entity) {
        this.scroller = block_entity;
    }

    @Override
    public void attach(@Nonnull IComputerAccess computer) {
        pcs.add(computer);
    }

    @Override
    public void detach(@Nonnull IComputerAccess computer) {
        pcs.removeIf(p -> (p.getID() == computer.getID()));
    }

    public void newValue() {
        for (IComputerAccess pc : pcs)
            pc.queueEvent("scroller_changed", pc.getAttachmentName());
    }

    public boolean getToggleState() { return togglestate; }


    @LuaFunction
    public final void isLocked(boolean state) {
        togglestate = !state;
    }

    @LuaFunction
    public final int getValue() throws LuaException {
        ScrollValueBehaviour scrollValueBehaviour = scroller.getBehaviour();
        if (scrollValueBehaviour == null) throw new LuaException("Internal Error! Could not get \'ScrollValueBehaviour\'!");

        return scrollValueBehaviour.getValue();
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
