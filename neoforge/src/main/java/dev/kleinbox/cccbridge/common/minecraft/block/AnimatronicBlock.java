package dev.kleinbox.cccbridge.common.minecraft.block;

import com.mojang.serialization.MapCodec;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import dev.kleinbox.cccbridge.common.minecraft.blockEntity.AnimatronicBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimatronicBlock extends HorizontalDirectionalBlock implements EntityBlock, IWrenchable {
    public static final BooleanProperty IS_DRIVER = BooleanProperty.create("is_driver");

    public AnimatronicBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(IS_DRIVER,false)
                .setValue(HorizontalDirectionalBlock.FACING, Direction.NORTH)
        );
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new AnimatronicBlockEntity(pos, state);
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState state, @NotNull BlockGetter view, @NotNull BlockPos pos, @NotNull CollisionContext ctx) {
        return Block.box(2, 0, 2, 14, 2, 14);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        BlockEntity blockentity = level.getBlockEntity(pos);
        if (!(blockentity instanceof AnimatronicBlockEntity animatronicBlockEntity))
            return;

        animatronicBlockEntity.setBodyPose(15F, 0F, 0F);
        animatronicBlockEntity.setHeadPose(20F, 0F, 0F);
        animatronicBlockEntity.setRightArmPose(-15F, 0F, 0F);
        animatronicBlockEntity.setLeftArmPose(-15F, 0F, 0F);
        animatronicBlockEntity.setChanged();
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        BlockState newState = state.setValue(HorizontalDirectionalBlock.FACING, (state.getValue(HorizontalDirectionalBlock.FACING).getClockWise()));
        return IWrenchable.super.onWrenched(newState, context);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager
                .add(IS_DRIVER)
                .add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public BlockState getStateForPlacement(@NotNull BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(IS_DRIVER, false)
                .setValue(HorizontalDirectionalBlock.FACING, ctx.getHorizontalDirection());
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return simpleCodec(AnimatronicBlock::new);
    }
}
