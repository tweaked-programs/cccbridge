package cc.tweaked_programs.cccbridge.common.minecraft.blockEntity;

import cc.tweaked_programs.cccbridge.client.animatronic.RustyMovement;
import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.assistance.animatronic.Face;
import cc.tweaked_programs.cccbridge.common.assistance.animatronic.Transition;
import cc.tweaked_programs.cccbridge.common.computercraft.peripherals.AnimatronicPeripheral;
import dan200.computercraft.api.peripheral.IPeripheral;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimatronicBlockEntity extends BlockEntity implements PeripheralBlockEntity {
    private Rotations headPose = new Rotations(0, 0, 0);
    private Rotations bodyPose = new Rotations(0,0,0);
    private Rotations leftArmPose = new Rotations(0,0,0);
    private Rotations rightArmPose = new Rotations(0,0,0);

    private Rotations current_headPose = new Rotations(0,0,0);
    private Rotations current_bodyPose = new Rotations(0,0,0);
    private Rotations current_leftArmPose = new Rotations(0,0,0);
    private Rotations current_rightArmPose = new Rotations(0,0,0);

    private Rotations start_headPose = new Rotations(0,0,0);
    private Rotations start_bodyPose = new Rotations(0,0,0);
    private Rotations start_leftArmPose = new Rotations(0,0,0);
    private Rotations start_rightArmPose = new Rotations(0,0,0);

    private Transition transition;
    private boolean isMoving;
    private double step;
    private long start_animation;

    private Face face;

    private AnimatronicPeripheral peripheral;

    public AnimatronicBlockEntity(BlockPos pos, BlockState blockState) {
        super((BlockEntityType<AnimatronicBlockEntity>) CCCRegistries.ANIMATRONIC_BLOCK_ENTITY.get(), pos, blockState);

        transition = Transition.RUSTY;
        isMoving = true;
        step = 0.0;
        face = Face.NORMAL;
        start_animation = 0;
    }

    @OnlyIn(Dist.CLIENT)
    public void updateCurrentPoses(float partialTicks) {
        step = (getLevel().getGameTime() - start_animation + partialTicks) * (0.0175 * 6);

        if (step >= 1) {
            isMoving = false;
            current_headPose = getDestinationHeadPose();
            current_bodyPose = getDestinationBodyPose();
            current_leftArmPose = getDestinationLeftArmPose();
            current_rightArmPose = getDestinationRightArmPose();
        } else {
            switch (transition) {
                case RUSTY -> {
                    current_headPose = updateRustyPose(start_headPose, getDestinationHeadPose(), false);
                    current_bodyPose = updateRustyPose(start_bodyPose, getDestinationBodyPose(), true);
                    current_leftArmPose = updateRustyPose(start_leftArmPose, getDestinationLeftArmPose(), false);
                    current_rightArmPose = updateRustyPose(start_rightArmPose, getDestinationRightArmPose(), false);
                }
                case NONE -> {
                    current_headPose = getDestinationHeadPose();
                    current_bodyPose = getDestinationBodyPose();
                    current_leftArmPose = getDestinationLeftArmPose();
                    current_rightArmPose = getDestinationRightArmPose();
                }
                case LINEAR -> {
                    current_headPose = updateLinearPose(start_headPose, getDestinationHeadPose(), false);
                    current_bodyPose = updateLinearPose(start_bodyPose, getDestinationBodyPose(), true);
                    current_leftArmPose = updateLinearPose(start_leftArmPose, getDestinationLeftArmPose(), false);
                    current_rightArmPose = updateLinearPose(start_rightArmPose, getDestinationRightArmPose(), false);
                }
                default -> {
                    setTransition(Transition.RUSTY); // For old Animatronics
                    // Will simply update next frame instead
                }
            }
        }
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

    @Override
    public void load(@NotNull CompoundTag nbt) {
        ListTag ltHead = nbt.getList("headPose", 5);
        Rotations headRot = ltHead.isEmpty() ? new Rotations(0,0,0) : new Rotations(ltHead);
        this.setHeadPose(headRot.getX(), headRot.getY(), headRot.getZ());

        ListTag ltBody = nbt.getList("bodyPose", 5);
        Rotations bodyRot = ltBody.isEmpty() ? new Rotations(0,0,0) : new Rotations(ltBody);
        this.setBodyPose(bodyRot.getX(), bodyRot.getY(), bodyRot.getZ());

        ListTag ltLeftArm = nbt.getList("leftArmPose", 5);
        Rotations leftArmRot = ltLeftArm.isEmpty() ? new Rotations(0,0,0) : new Rotations(ltLeftArm);
        this.setLeftArmPose(leftArmRot.getX(), leftArmRot.getY(), leftArmRot.getZ());

        ListTag ltRightArmPose = nbt.getList("rightArmPose", 5);
        Rotations rightArmRot = ltRightArmPose.isEmpty() ? new Rotations(0,0,0) : new Rotations(ltRightArmPose);
        this.setRightArmPose(rightArmRot.getX(), rightArmRot.getY(), rightArmRot.getZ());

        @Nullable Face face = Face.contains(nbt.getString("face"));
        setFace((face != null) ? face : Face.NORMAL);

        @Nullable Transition transition = Transition.contains(nbt.getString("transition"));
        setTransition((transition != null) ? transition : Transition.RUSTY);

        super.load(nbt);

        if (getLevel() != null && getLevel().isClientSide)
            startAnimation();
    }

    @Override
    public void saveAdditional(@NotNull CompoundTag nbt) {
        nbt.put("headPose", getDestinationHeadPose().save());
        nbt.put("bodyPose", getDestinationBodyPose().save());
        nbt.put("leftArmPose", getDestinationLeftArmPose().save());
        nbt.put("rightArmPose", getDestinationRightArmPose().save());
        if (face != null)
            nbt.putString("face", face.getId());
        if (transition != null)
            nbt.putString("transition", transition.getId());

        super.saveAdditional(nbt);
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (getLevel() != null) {
            if (getLevel().isClientSide)
                startAnimation();
            else
                getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), Block.UPDATE_ALL); // Should I use UPDATE_CLIENTS instead? Couldn't test this yet
        }
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

    private Rotations updateRustyPose(Rotations start, Rotations destination, boolean lazyWay) {
        float x = RustyMovement.updateMovement(start.getX(), destination.getX(), step, false);
        float y = RustyMovement.updateMovement(start.getY(), destination.getY(), step, lazyWay);
        float z = RustyMovement.updateMovement(start.getZ(), destination.getZ(), step, false);
        return new Rotations(x, y, z);
    }

    private Rotations updateLinearPose(Rotations start, Rotations destination, boolean lazyWay) {
        float x = (float) (start.getX() + (destination.getX()-start.getX()) * step);
        float y = (float) (start.getY() + (destination.getY()-start.getY()) * step);
        float z = (float) (start.getZ() + (destination.getZ()-start.getZ()) * step);
        return new Rotations(x, y, z);
    }

    public boolean isMoving() {
        return isMoving;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public Level getLevel() {
        Level level = super.getLevel();
        if (level == null)
            level = Minecraft.getInstance().level;
        return level;
    }

    @OnlyIn(Dist.CLIENT)
    public void startAnimation() {
        // level is null during world loading; skip animation
        if (getLevel() == null) {
            current_headPose = getHeadPose();
            current_bodyPose = getBodyPose();
            current_leftArmPose = getLeftArmPose();
            current_rightArmPose = getRightArmPose();
            return;
        }

        start_animation = getLevel().getGameTime();
        start_headPose = current_headPose;
        start_bodyPose = current_bodyPose;
        start_leftArmPose = current_leftArmPose;
        start_rightArmPose = current_rightArmPose;
        isMoving = true;
        step = 0.0;
    }

    public void setFace(Face face) {
        this.face = face;
    }

    public void setTransition(Transition animationMode) {
        this.transition = animationMode;
    }

    public void setRightArmPose(float x, float y, float z) {
        this.rightArmPose = new Rotations(x, y, z);
    }

    public void setLeftArmPose(float x, float y, float z) {
        this.leftArmPose = new Rotations(x, y, z);
    }

    public void setBodyPose(float x, float y, float z) {
        this.bodyPose = new Rotations(x, y, z);
    }

    public void setHeadPose(float x, float y, float z) {
        this.headPose = new Rotations(x, y, z);
    }

    public Face getFace() {
        return face;
    }

    public Rotations getRightArmPose() {
        return current_rightArmPose;
    }

    public Rotations getLeftArmPose() {
        return current_leftArmPose;
    }

    public Rotations getBodyPose() {
        return current_bodyPose;
    }

    public Rotations getHeadPose() {
        return current_headPose;
    }

    public Rotations getDestinationHeadPose() {
        return headPose;
    }

    public Rotations getDestinationBodyPose() {
        return bodyPose;
    }

    public Rotations getDestinationLeftArmPose() {
        return leftArmPose;
    }

    public Rotations getDestinationRightArmPose() {
        return rightArmPose;
    }
}
