package cc.tweaked_programs.cccbridge.block.redrouter;

import cc.tweaked_programs.cccbridge.CCCBridge;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.peripheral.IPeripheralTile;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.Packet;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedRouterBlockEntity extends BlockEntity implements IPeripheralTile {
    private HashMap<String, Integer> outputDir = new HashMap<>();
    private HashMap<String, Integer> inputDir = new HashMap<>();
    private RedRouterBlockPeripheral peripheral;
    private boolean blockupdate = false;
    private Direction facing;

    public RedRouterBlockEntity(BlockPos pos, BlockState state) {
        super(CCCBridge.REDROUTER_BLOCK_ENTITY, pos, state);
        facing = state.get(Properties.HORIZONTAL_FACING);
        outputDir.put("up", 0);
        outputDir.put("down", 0);
        outputDir.put("north", 0);
        outputDir.put("east", 0);
        outputDir.put("south", 0);
        outputDir.put("west", 0);

        inputDir.put("up", 0);
        inputDir.put("down", 0);
        inputDir.put("north", 0);
        inputDir.put("east", 0);
        inputDir.put("south", 0);
        inputDir.put("west", 0);
    }

    public static void tick(World world, BlockPos blockPos, BlockState state, BlockEntity be) {
        if (!(be instanceof RedRouterBlockEntity redrouter)) return;
        if (state.get(Properties.HORIZONTAL_FACING) != redrouter.facing) {
            redrouter.blockupdate = true;
            redrouter.facing = state.get(Properties.HORIZONTAL_FACING);
        }

        if (redrouter.blockupdate) {
            world.updateNeighbors(be.getPos(), world.getBlockState(be.getPos())
                    .getBlock());
            redrouter.blockupdate = false;
        }

        updateInputs(world, blockPos, state, redrouter);
    }

    private static void updateInputs(World world, BlockPos blockPos, BlockState state, RedRouterBlockEntity redrouter) {
        for (Map.Entry<String,Integer> entry : redrouter.inputDir.entrySet()) {
            String side = entry.getKey();
            Direction dir = Direction.byName(side).getOpposite();
            BlockPos offsetPos = blockPos.offset(dir);
            BlockState block = world.getBlockState(offsetPos);
            int power = block.getBlock().getWeakRedstonePower(block, world, offsetPos, dir);
            redrouter.inputDir.put(side,power);
        }
    }

    public Direction getFacing() {
        if (facing == null) {
            return Direction.NORTH;
        }
        return facing;
    }

    @Override
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new RedRouterBlockPeripheral(this);
        return peripheral;
    }

    public int getRedstoneInput(Direction side) {
        int value = 0;
        try {
            value = inputDir.get(side.asString());
        } catch(NullPointerException e) { }
        return value;
    }

    public int getPower(Direction side) {
        int value = 0;
        try {
            value = outputDir.get(side.asString());
        } catch(NullPointerException e) { }
        return value;
    }

    public void setPower(String side, int power) {
        outputDir.put(side, power);
        markDirty();
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        for (Map.Entry<String,Integer> entry : outputDir.entrySet()) {
            String side = entry.getKey();
            outputDir.put(side, nbt.getInt(side));
        }
    }

    @Override
    public void writeNbt(NbtCompound nbt) {
        for (Map.Entry<String,Integer> entry : outputDir.entrySet())
            nbt.putInt(entry.getKey(), entry.getValue());
        super.writeNbt(nbt);
    }

    @Override
    public void markDirty() {
        super.markDirty();
        if (this.hasWorld() && !world.isClient()) {
            blockupdate = true;
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
