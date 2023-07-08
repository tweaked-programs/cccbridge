package cc.tweaked_programs.cccbridge.peripherals;

import cc.tweaked_programs.cccbridge.blockEntity.AnimatronicBlockEntity;
import dan200.computercraft.api.lua.LuaException;
import dan200.computercraft.api.lua.LuaFunction;
import dan200.computercraft.api.lua.MethodResult;
import net.minecraft.core.Rotations;
import net.minecraft.util.Mth;

/**
 * This peripheral is used by the Animatronic. It is an electronic puppet that can be positioned however needed.
 * Neat for decoration purposes! A bit rusty, however.
 *
 * @version 1.1
 */
public class AnimatronicPeripheral extends TweakedPeripheral<AnimatronicBlockEntity> {
    private float[] headRot;
    private float[] bodyRot;
    private float[] leftArmRot;
    private float[] rightArmRot;

    public AnimatronicPeripheral(AnimatronicBlockEntity blockEntity) {
        super("animatronic", blockEntity);

        headRot = new float[]{0,0,0};
        bodyRot = new float[]{0,0,0};
        leftArmRot = new float[]{0,0,0};
        rightArmRot = new float[]{0,0,0};
    }

    /**
     * Changes the face of the Animatronic.
     *
     * @param face The new face. Must be either 'normal', 'happy', 'question' or 'sad'.
     *
     * @throws LuaException Whenever the given string is not one of those types.
     */
    @LuaFunction
    public final void setFace(String face) throws LuaException {
        if (face.equals("normal") || face.equals("happy") || face.equals("question") || face.equals("sad")) {
            AnimatronicBlockEntity be = super.getTarget();
            if (be != null) {
                be.setFace(face);
                be.setChanged();
            }
        } else throw new LuaException("Given string must be either 'normal', 'happy', 'question' or 'sad'");
    }

    /**
     * Pushes the stored rotation values to the Animatronic.
     * After pushing them, the rotations get reset to 0 everywhere.
     */
    @LuaFunction
    public final void push() {
        AnimatronicBlockEntity be = super.getTarget();
        if (be != null) {
            be.setHeadPose(headRot[0], headRot[1], headRot[2]);
            be.setBodyPose(bodyRot[0], bodyRot[1], bodyRot[2]);
            be.setLeftArmPose(leftArmRot[0], leftArmRot[1], leftArmRot[2]);
            be.setRightArmPose(rightArmRot[0], rightArmRot[1], rightArmRot[2]);
            be.setChanged();
        }

        headRot = new float[]{0,0,0};
        bodyRot = new float[]{0,0,0};
        leftArmRot = new float[]{0,0,0};
        rightArmRot = new float[]{0,0,0};
    }

    /**
     * Sets the head rotation.
     * Can only be set within the bounds -180° to 180° for `X`, `Y` and `Z`.
     *
     * @param x The X rotation.
     * @param y The Y rotation.
     * @param z The Z rotation.
     */
    @LuaFunction
    public final void setHeadRot(double x, double y, double z) {
        x = Mth.clamp(x, -180D, 180D);
        y = Mth.clamp(y, -180D, 180D);
        z = Mth.clamp(z, -180D, 180D);
        headRot = new float[]{(float)x, (float)y, (float)z};
    }

    /**
     * Sets the body rotation.
     * Can only be set within the bounds -180° to 180° for `Y` and `Z`;
     * > Note: `X` can be set within 360°.
     *
     * @param x The X rotation.
     * @param y The Y rotation.
     * @param z The Z rotation.
     */
    @LuaFunction
    public final void setBodyRot(double x, double y, double z) {
        x = Mth.clamp(x, -360D, 360D);
        y = Mth.clamp(y, -180D, 180D);
        z = Mth.clamp(z, -180D, 180D);
        bodyRot = new float[]{(float)x, (float)y, (float)z};
    }

    /**
     * Sets the left arm rotation.
     * Can only be set within the bounds -180° to 180° for `X`, `Y` and `Z`.
     *
     * @param x The X rotation.
     * @param y The Y rotation.
     * @param z The Z rotation.
     */
    @LuaFunction
    public final void setLeftArmRot(double x, double y, double z) {
        x = Mth.clamp(x, -180, 180);
        y = Mth.clamp(y, -180, 180);
        z = Mth.clamp(z, -180, 180);
        leftArmRot = new float[]{(float)x, (float)y, (float)z};
    }

    /**
     * Sets the right arm rotation.
     * Can only be set within the bounds -180° to 180° for `X`, `Y` and `Z`.
     *
     * @param x The X rotation.
     * @param y The Y rotation.
     * @param z The Z rotation.
     */
    @LuaFunction
    public final void setRightArmRot(double x, double y, double z) {
        x = Mth.clamp(x, -180, 180);
        y = Mth.clamp(y, -180, 180);
        z = Mth.clamp(z, -180, 180);
        rightArmRot = new float[]{(float)x, (float)y, (float)z};
    }

    /**
     * Returns the current stored head rotation.
     *
     * @return Head rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getStoredHeadRot() {
        return MethodResult.of((double)headRot[0], (double)headRot[1], (double)headRot[2]);
    }

    /**
     * Returns the current stored body rotation.
     *
     * @return Body rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getStoredBodyRot() {
        return MethodResult.of((double)bodyRot[0], (double)bodyRot[1], (double)bodyRot[2]);
    }

    /**
     * Returns the current stored left arm rotation.
     *
     * @return Left arm rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getStoredLeftArmRot() {
        return MethodResult.of((double)leftArmRot[0], (double)leftArmRot[1], (double)leftArmRot[2]);
    }

    /**
     * Returns the current stored right arm rotation.
     *
     * @return Right arm rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getStoredRightArmRot() {
        return MethodResult.of((double)rightArmRot[0], (double)rightArmRot[1], (double)rightArmRot[2]);
    }

    /**
     * Returns the head rotation of the Animatronic.
     *
     * @return Head rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getAppliedHeadRot() {
        AnimatronicBlockEntity be = super.getTarget();
        if (be != null) {
            Rotations rot = be.getHeadPose();
            return MethodResult.of((double)rot.getX(), (double)rot.getY(), (double)rot.getZ());
        }
        return MethodResult.of(0F, 0F, 0F);
    }

    /**
     * Returns the body rotation of the Animatronic.
     *
     * @return Body rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getAppliedBodyRot() {
        AnimatronicBlockEntity be = super.getTarget();
        if (be != null) {
            Rotations rot = be.getBodyPose();
            return MethodResult.of((double)rot.getX(), (double)rot.getY(), (double)rot.getZ());
        }
        return MethodResult.of(0F, 0F, 0F);
    }

    /**
     * Returns the left arm rotation of the Animatronic.
     *
     * @return Left arm rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getAppliedLeftArmRot() {
        AnimatronicBlockEntity be = super.getTarget();
        if (be != null) {
            Rotations rot = be.getLeftArmPose();
            return MethodResult.of((double)rot.getX(), (double)rot.getY(), (double)rot.getZ());
        }
        return MethodResult.of(0F, 0F, 0F);
    }

    /**
     * Returns the right arm rotation of the Animatronic.
     *
     * @return Right arm rotation: table.unpack{X, Y, Z}.
     */
    @LuaFunction
    public final MethodResult getAppliedRightArmRot() {
        AnimatronicBlockEntity be = super.getTarget();
        if (be != null) {
            Rotations rot = be.getRightArmPose();
            return MethodResult.of((double)rot.getX(), (double)rot.getY(), (double)rot.getZ());
        }
        return MethodResult.of(0F, 0F, 0F);
    }
}
