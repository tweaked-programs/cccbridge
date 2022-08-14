package cc.tweaked_programs.cccbridge.block.redrouter;

import cc.tweaked_programs.cccbridge.Main;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedRouterBlockEntity extends BlockEntity implements IPeripheralTile {
    private HashMap<String, Integer> dir = new HashMap<>();
    public static BooleanProperty POWERED;
    private RedRouterBlockPeripheral peripheral;
    private RedRouterBlock block;

    public RedRouterBlockEntity(BlockPos pos, BlockState state) {
        super(Main.REDROUTER_BLOCK_ENTITY, pos, state);
        block = (RedRouterBlock)state.getBlock();
        dir.put("up", 0);
        dir.put("down", 0);
        dir.put("north", 0);
        dir.put("east", 0);
        dir.put("south", 0);
        dir.put("west", 0);
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new RedRouterBlockPeripheral(this);
        return peripheral;
    }

    public int getPoweredSide(Direction side) {
        int value = 0;
        try {
            value = dir.get(side.asString());
        } catch(NullPointerException e) { }
        return value;
    }

    public void setPoweredSide(String side, int power) {
        dir.put(side, power);
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        for (Map.Entry<String,Integer> entry : dir.entrySet()) {
            String side = entry.getKey();
            dir.put(side, nbt.getInt(side));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        for (Map.Entry<String,Integer> entry : dir.entrySet())
            nbt.putInt(entry.getKey(), entry.getValue());
        super.writeNbt(nbt);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.hasWorld() && !world.isClient()) {
            world.updateNeighbors(pos, world.getBlockState(pos).getBlock());
        }
    }

    @Nullable
    @Override
    public Packet<ClientPlayPacketListener> toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}
