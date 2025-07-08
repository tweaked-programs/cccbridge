package dev.kleinbox.cccbridge.common.minecraft.blockEntity;

import dan200.computercraft.api.peripheral.IPeripheral;
import dev.kleinbox.cccbridge.common.CCCRegistries;
import dev.kleinbox.cccbridge.common.computercraft.peripherals.RedRouterBlockPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RedRouterBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private final HashMap<String, Integer> outputDir = new HashMap<>();
    private final HashMap<String, Integer> inputDir = new HashMap<>();
    private boolean blockupdate = false;
    private boolean newInputs = false;
    private RedRouterBlockPeripheral peripheral;
    private Direction facing;

    public RedRouterBlockEntity(BlockPos pos, BlockState state) {
        super((BlockEntityType<RedRouterBlockEntity>) CCCRegistries.REDROUTER_BLOCK_ENTITY.get(), pos, state);
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
            world.updateNeighborsAt(blockPos, state.getBlock());
            // Update blocks next to ours as well because redstone is great
            for (Direction dir : Direction.values()) {
                BlockPos relative = blockPos.relative(dir);
                world.updateNeighborsAt(relative, world.getBlockState(relative).getBlock());
            }

            redrouter.blockupdate = false;
        }
        updateInputs(world, blockPos, redrouter);

        if (redrouter.newInputs && redrouter.peripheral != null) {
            redrouter.peripheral.sendEvent(RedRouterBlockPeripheral.REDSTONE_EVENT);
            redrouter.newInputs = false;
        }
    }

    public static void updateInputs(Level world, BlockPos blockPos, RedRouterBlockEntity redrouter) {
        for (Map.Entry<String, Integer> entry : redrouter.inputDir.entrySet()) {
            String side = entry.getKey();
            Direction dir = Direction.byName(side).getOpposite();
            BlockPos offsetPos = blockPos.relative(dir);
            BlockState block = world.getBlockState(offsetPos);

            int power = block.getSignal(world, offsetPos, dir);

            if (redrouter.inputDir.get(side) != power) { redrouter.newInputs = true; }

            redrouter.inputDir.put(side, power);
        }
    }

    public Direction getFacing() {
        if (facing == null) {
            return Direction.NORTH;
        }
        return facing;
    }

    public int getRedstoneInput(Direction side) {
        try {
            return inputDir.get(side.getName());
        } catch (NullPointerException ignored) {
            return 0;
        }
    }

    public int getPower(Direction side) {
        try {
            return outputDir.get(side.getName());
        } catch (NullPointerException ignored) {
            return 0;
        }
    }

    public void setPower(String side, int power) {
        outputDir.put(side, power);
        setChanged();
    }

    @Override
    protected void loadAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        for (Map.Entry<String, Integer> entry : outputDir.entrySet()) {
            String side = entry.getKey();
            outputDir.put(side, tag.getInt(side));
        }

        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag tag, HolderLookup.@NotNull Provider registries) {
        for (Map.Entry<String, Integer> entry : outputDir.entrySet())
            tag.putInt(entry.getKey(), entry.getValue());

        super.saveAdditional(tag, registries);
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
    public @NotNull CompoundTag getUpdateTag(HolderLookup.@NotNull Provider registries) {
        return saveWithoutMetadata(registries);
    }

    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (peripheral == null)
            peripheral = new RedRouterBlockPeripheral(this);
        return peripheral;
    }
}