package cc.tweaked_programs.cccbridge.entity.animatronic;

import cc.tweaked_programs.cccbridge.Misc;
import net.minecraft.core.Rotations;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

public class AnimatronicEntity extends Entity {
    private static final Rotations DEFAULT_HEAD_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_BODY_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_LEFT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);
    private static final Rotations DEFAULT_RIGHT_ARM_POSE = new Rotations(0.0F, 0.0F, 0.0F);

    public static final EntityDataAccessor<Rotations> DATA_HEAD_POSE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.ROTATIONS);
    public static final EntityDataAccessor<Rotations> DATA_BODY_POSE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.ROTATIONS);
    public static final EntityDataAccessor<Rotations> DATA_LEFT_ARM_POSE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.ROTATIONS);
    public static final EntityDataAccessor<Rotations> DATA_RIGHT_ARM_POSE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.ROTATIONS);
    public static final EntityDataAccessor<String> DATA_FACE = SynchedEntityData.defineId(AnimatronicEntity.class, EntityDataSerializers.STRING);


    private Rotations headPose = new Rotations(DEFAULT_HEAD_POSE.getX(), DEFAULT_HEAD_POSE.getY(), DEFAULT_HEAD_POSE.getZ());
    private Rotations bodyPose = new Rotations(DEFAULT_BODY_POSE.getX(), DEFAULT_BODY_POSE.getY(), DEFAULT_BODY_POSE.getZ());
    private Rotations leftArmPose = new Rotations(DEFAULT_LEFT_ARM_POSE.getX(), DEFAULT_LEFT_ARM_POSE.getY(), DEFAULT_LEFT_ARM_POSE.getZ());
    private Rotations rightArmPose = new Rotations(DEFAULT_RIGHT_ARM_POSE.getX(), DEFAULT_RIGHT_ARM_POSE.getY(), DEFAULT_RIGHT_ARM_POSE.getZ());

    private Rotations current_headPose = new Rotations(DEFAULT_HEAD_POSE.getX(), DEFAULT_HEAD_POSE.getY(), DEFAULT_HEAD_POSE.getZ());
    private Rotations current_bodyPose = new Rotations(DEFAULT_BODY_POSE.getX(), DEFAULT_BODY_POSE.getY(), DEFAULT_BODY_POSE.getZ());
    private Rotations current_leftArmPose = new Rotations(DEFAULT_LEFT_ARM_POSE.getX(), DEFAULT_LEFT_ARM_POSE.getY(), DEFAULT_LEFT_ARM_POSE.getZ());
    private Rotations current_rightArmPose = new Rotations(DEFAULT_RIGHT_ARM_POSE.getX(), DEFAULT_RIGHT_ARM_POSE.getY(), DEFAULT_RIGHT_ARM_POSE.getZ());

    private Rotations start_headPose = new Rotations(DEFAULT_HEAD_POSE.getX(), DEFAULT_HEAD_POSE.getY(), DEFAULT_HEAD_POSE.getZ());
    private Rotations start_bodyPose = new Rotations(DEFAULT_BODY_POSE.getX(), DEFAULT_BODY_POSE.getY(), DEFAULT_BODY_POSE.getZ());
    private Rotations start_leftArmPose = new Rotations(DEFAULT_LEFT_ARM_POSE.getX(), DEFAULT_LEFT_ARM_POSE.getY(), DEFAULT_LEFT_ARM_POSE.getZ());
    private Rotations start_rightArmPose = new Rotations(DEFAULT_RIGHT_ARM_POSE.getX(), DEFAULT_RIGHT_ARM_POSE.getY(), DEFAULT_RIGHT_ARM_POSE.getZ());

    private boolean isMoving;
    private double step;
    private long start_animation;

    private String face;

    public AnimatronicEntity(EntityType<?> entityType, Level level) {
        super(entityType, level);
        isMoving = true;
        step = 0.0;
        face = "normal";
        start_animation = 0;
    }

    public void updateCurrentPoses(float partialTicks) {
        step = (level.getGameTime()-start_animation+partialTicks) * (0.0175*6);

        current_headPose = updatePose(start_headPose, getDestinationHeadPose());
        current_bodyPose = updatePose(start_bodyPose, getDestinationBodyPose());
        current_leftArmPose = updatePose(start_leftArmPose, getDestinationLeftArmPose());
        current_rightArmPose = updatePose(start_rightArmPose, getDestinationRightArmPose());

        if (step >= 1) {
            isMoving = false;
            current_headPose = getDestinationHeadPose();
            current_bodyPose = getDestinationBodyPose();
            current_leftArmPose = getDestinationLeftArmPose();
            current_rightArmPose = getDestinationRightArmPose();
        }
    }

    private Rotations updatePose(Rotations start, Rotations destination) {
        float x = Misc.updateMovement(start.getX(), destination.getX(), step);
        float y = Misc.updateMovement(start.getY(), destination.getY(), step);
        float z = Misc.updateMovement(start.getZ(), destination.getZ(), step);
        return new Rotations(x, y, z);
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void startAnimation() {
        // onLoad level is null; skip animation
        if (level == null) {
            current_headPose = getDestinationHeadPose();
            current_bodyPose = getDestinationBodyPose();
            current_leftArmPose = getDestinationLeftArmPose();
            current_rightArmPose = getRightArmPose();
            return;
        }

        if (!level.isClientSide)
            return;

        start_animation = level.getGameTime();
        start_headPose = current_headPose;
        start_bodyPose = current_bodyPose;
        start_leftArmPose = current_leftArmPose;
        start_rightArmPose = current_rightArmPose;
        isMoving = true;
        step = 0.0;
    }

    public ResourceLocation getFace() {
        if (this.entityData.get(DATA_FACE) == null)
            return AnimatronicRenderer.TEXTURE_FACE_NORMAL;

        return switch (this.entityData.get(DATA_FACE)) {
            case "normal" -> AnimatronicRenderer.TEXTURE_FACE_NORMAL;
            case "happy" -> AnimatronicRenderer.TEXTURE_FACE_HAPPY;
            case "question" -> AnimatronicRenderer.TEXTURE_FACE_QUESTION;
            case "sad" -> AnimatronicRenderer.TEXTURE_FACE_SAD;

            default -> AnimatronicRenderer.TEXTURE_FACE_NORMAL;
        };
    }

    public void setFace(String face) {
        this.entityData.set(DATA_FACE, face);
    }

    public void setRightArmPose(Rotations rotations) {
        this.entityData.set(DATA_RIGHT_ARM_POSE, rotations);
    }

    public void setLeftArmPose(Rotations rotations) {
        this.entityData.set(DATA_LEFT_ARM_POSE, rotations);
    }

    public void setBodyPose(Rotations rotations) {
        this.entityData.set(DATA_BODY_POSE, rotations);
    }

    public void forceBodyPose(Rotations rotations) {
        setBodyPose(rotations);
        current_bodyPose = rotations;
    }


    public void setHeadPose(Rotations rotations) {
        this.entityData.set(DATA_HEAD_POSE, rotations);

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
        return Optional.ofNullable(this.entityData.get(DATA_HEAD_POSE)).orElse(DEFAULT_HEAD_POSE);
    }

    public Rotations getDestinationBodyPose() {
        return Optional.ofNullable(this.entityData.get(DATA_BODY_POSE)).orElse(DEFAULT_BODY_POSE);
    }

    public Rotations getDestinationLeftArmPose() {
        return Optional.ofNullable(this.entityData.get(DATA_LEFT_ARM_POSE)).orElse(DEFAULT_LEFT_ARM_POSE);
    }

    public Rotations getDestinationRightArmPose() {
        return Optional.ofNullable(this.entityData.get(DATA_RIGHT_ARM_POSE)).orElse(DEFAULT_RIGHT_ARM_POSE);
    }


    public void read(CompoundTag nbt) {
        readAdditionalSaveData(nbt);
    }

    public void add(CompoundTag nbt) {
        addAdditionalSaveData(nbt);
    }

    @Override
    protected void readAdditionalSaveData(CompoundTag nbt) {
        ListTag ltHead = nbt.getList("headPose", 5);
        this.setHeadPose(ltHead.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(ltHead));

        ListTag ltBody = nbt.getList("bodyPose", 5);
        this.setBodyPose(ltBody.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(ltBody));

        ListTag ltLeftArm = nbt.getList("leftArmPose", 5);
        this.setLeftArmPose(ltLeftArm.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(ltLeftArm));

        ListTag ltArmPose = nbt.getList("rightArmPose", 5);
        this.setRightArmPose(ltArmPose.isEmpty() ? DEFAULT_HEAD_POSE : new Rotations(ltArmPose));

        setFace(nbt.getString("face"));

        startAnimation();
    }

    @Override
    protected void addAdditionalSaveData(CompoundTag nbt) {
        nbt.put("headPose", getDestinationHeadPose().save());
        nbt.put("bodyPose", getDestinationBodyPose().save());
        nbt.put("leftArmPose", getDestinationLeftArmPose().save());
        nbt.put("rightArmPose", getDestinationRightArmPose().save());
        if (this.entityData.get(DATA_FACE) != null)
            nbt.putString("face", this.entityData.get(DATA_FACE));
    }

    @Override
    protected void defineSynchedData() {
        this.entityData.define(DATA_HEAD_POSE, headPose);
        this.entityData.define(DATA_BODY_POSE, bodyPose);
        this.entityData.define(DATA_LEFT_ARM_POSE, leftArmPose);
        this.entityData.define(DATA_RIGHT_ARM_POSE, rightArmPose);
        this.entityData.define(DATA_FACE, face);
    }

    @Override
    public @NotNull Packet<?> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        super.onSyncedDataUpdated(key);
        startAnimation();
    }
}
