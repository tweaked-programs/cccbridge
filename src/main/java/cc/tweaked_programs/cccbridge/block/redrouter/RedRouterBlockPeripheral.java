package cc.tweaked_programs.cccbridge.block.redrouter;

import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RedRouterBlockPeripheral implements IPeripheral {
    private final RedRouterBlockEntity redrouter_block_entity;

    RedRouterBlockPeripheral(RedRouterBlockEntity redrouter_block_entity) { this.redrouter_block_entity = redrouter_block_entity; }

    @NotNull
    @Override
    public String getType() {
        return "redrouter";
    }

    @LuaFunction
    public final void setRedstoneOutput(String side, boolean status) {
        if (Direction.byName(side) != null)
            redrouter_block_entity.setPoweredSide(side, status ? 15 : 0);
    }

    @LuaFunction
    public final void setAnalogOutput(String side, int power) {
        if (Direction.byName(side) != null) {
            if (power < 0) power = 0;
            else if (power > 15) power = 15;
            redrouter_block_entity.setPoweredSide(side, power);
        }
    }

    @Override
    public boolean equals(@Nullable IPeripheral other) {
        return this == other || other instanceof RedRouterBlockPeripheral redrouter && redrouter.redrouter_block_entity == redrouter_block_entity;
    }
}
