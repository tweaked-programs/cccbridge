package cc.tweaked_programs.cccbridge.block.redrouter;

import cc.tweaked_programs.cccbridge.CCCBridge;
import cc.tweaked_programs.cccbridge.block.target.TargetBlockPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedRouterBlockEntity extends BlockEntity {
    private HashMap<String, Integer> outputDir = new HashMap<>();
    private HashMap<String, Integer> inputDir = new HashMap<>();
    private boolean blockupdate = false;
    private RedRouterBlockPeripheral peripheral;
    private Direction facing;

    public RedRouterBlockEntity(BlockPos pos, BlockState state) {
        super(CCCBridge.REDROUTER_BLOCK_ENTITY.get(), pos, state);
        facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
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

    public static void tick(Level world, BlockPos blockPos, BlockState state, BlockEntity be) {
        if (!(be instanceof RedRouterBlockEntity redrouter)) return;
        if (state.getValue(BlockStateProperties.HORIZONTAL_FACING) != redrouter.facing) {
            redrouter.blockupdate = true;
            redrouter.facing = state.getValue(BlockStateProperties.HORIZONTAL_FACING);
        }

        if (redrouter.blockupdate) {
            world.updateNeighborsAt(be.getBlockPos(), world.getBlockState(be.getBlockPos())
                    .getBlock());
            redrouter.blockupdate = false;
        }

        updateInputs(world, blockPos, state, redrouter);
    }

    private static void updateInputs(Level world, BlockPos blockPos, BlockState state, RedRouterBlockEntity redrouter) {
        for (Map.Entry<String,Integer> entry : redrouter.inputDir.entrySet()) {
            String side = entry.getKey();
            Direction dir = Direction.byName(side).getOpposite();
            BlockPos offsetPos = blockPos.relative(dir);
            BlockState block = world.getBlockState(offsetPos);
            int power = block.getBlock().getSignal(block, world, offsetPos, dir);
            redrouter.inputDir.put(side,power);
        }
    }

    public Direction getFacing() {
        if (facing == null) {
            return Direction.NORTH;
        }
        return facing;
    }

    public int getRedstoneInput(Direction side) {
        int value = 0;
        try {
            value = inputDir.get(side.getName());
        } catch(NullPointerException e) { }
        return value;
    }

    public int getPower(Direction side) {
        int value = 0;
        try {
            value = outputDir.get(side.getName());
        } catch(NullPointerException e) { }
        return value;
    }

    public void setPower(String side, int power) {
        outputDir.put(side, power);
        setChanged();
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        for (Map.Entry<String,Integer> entry : outputDir.entrySet()) {
            String side = entry.getKey();
            outputDir.put(side, nbt.getInt(side));
        }
    }

    @Override
    public void saveAdditional(CompoundTag nbt) {
        for (Map.Entry<String,Integer> entry : outputDir.entrySet())
            nbt.putInt(entry.getKey(), entry.getValue());
        super.saveAdditional(nbt);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        blockupdate = true;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public IPeripheral getPeripheral(Direction side) {
        if (peripheral == null)
            peripheral = new RedRouterBlockPeripheral(this);
        return peripheral;
    }
}
