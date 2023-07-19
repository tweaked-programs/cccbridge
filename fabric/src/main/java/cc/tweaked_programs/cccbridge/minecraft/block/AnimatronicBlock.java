package cc.tweaked_programs.cccbridge.minecraft.block;

import cc.tweaked_programs.cccbridge.minecraft.blockEntity.AnimatronicBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimatronicBlock extends Block implements EntityBlock {
    public AnimatronicBlock() {
        super(Properties.of(Material.METAL).strength(1.3f).sound(SoundType.CHAIN));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AnimatronicBlockEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return Shapes.join(Block.box(7.5, 2, 7.5, 8.5, 16, 8.5), Block.box(2, 0, 2, 14, 2, 14), BooleanOp.OR);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (!(blockentity instanceof AnimatronicBlockEntity animatronicBlockEntity))
            return;

        float rotation = (float) Mth.floor((Mth.wrapDegrees(placer.yRotO) + 22.5F) / 45.0F) * 45.0F;
        animatronicBlockEntity.forceBodyPose(0F, rotation, 0F);
        animatronicBlockEntity.setBodyPose(15F, rotation, 0F);
        animatronicBlockEntity.setHeadPose(20F, 0F, 0F);
        animatronicBlockEntity.setRightArmPose(-15F, 0F, 0F);
        animatronicBlockEntity.setLeftArmPose(-15F, 0F, 0F);
        animatronicBlockEntity.setChanged();
    }
}
