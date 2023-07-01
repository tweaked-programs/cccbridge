package cc.tweaked_programs.cccbridge.blockEntity;

import cc.tweaked_programs.cccbridge.CCCRegister;
import cc.tweaked_programs.cccbridge.entity.animatronic.AnimatronicEntity;
import cc.tweaked_programs.cccbridge.peripherals.AnimatronicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class AnimatronicBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private AnimatronicPeripheral peripheral;
    private AnimatronicEntity animatronic;

    public AnimatronicBlockEntity(BlockPos pos, BlockState blockState) {
        super(CCCRegister.getBlockEntityType("animatronic_block"), pos, blockState);
    }

    public Rotations getRightArmPose() {
        return getAnimatronic().getDestinationRightArmPose();
    }

    public Rotations getLeftArmPose() {
        return getAnimatronic().getDestinationLeftArmPose();
    }

    public Rotations getBodyPose() {
        return getAnimatronic().getDestinationBodyPose();
    }

    public Rotations getHeadPose() {
        return getAnimatronic().getDestinationHeadPose();
    }

    public void setRightArmPose(float x, float y, float z) {
        getAnimatronic().setRightArmPose(new Rotations(x, y, z));
    }

    public void setLeftArmPose(float x, float y, float z) {
        getAnimatronic().setLeftArmPose(new Rotations(x, y, z));
    }

    public void setBodyPose(float x, float y, float z) {
        getAnimatronic().setBodyPose(new Rotations(x, y, z));
    }

    public void setHeadPose(float x, float y, float z) {
        getAnimatronic().setHeadPose(new Rotations(x, y, z));
    }

    public void forceBodyPose(float x, float y, float z) {
        getAnimatronic().forceBodyPose(new Rotations(x, y, z));
    }

    @Override
    @Nullable
    public IPeripheral getPeripheral(@NotNull Direction side) {
        if (side.getName().equals(Direction.DOWN.getName())) {
            if (peripheral == null)
                peripheral = new AnimatronicPeripheral(this);
            return peripheral;
        }
        return null;
    }

    public AnimatronicEntity getAnimatronic() {
        if (animatronic == null)
            this.animatronic = new AnimatronicEntity(CCCRegister.getEntityType("animatronic"), getLevel());

        if (getLevel() != null)
            animatronic.level = getLevel();

        return animatronic;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        getAnimatronic().read(nbt);
        super.load(nbt);
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        getAnimatronic().add(nbt);
        super.saveAdditional(nbt);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null && this.hasLevel() && !level.isClientSide)
            level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL);
    }
    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    public void setFace(String face) {
        getAnimatronic().setFace(face);
    }
}
