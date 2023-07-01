package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.CCCRegister;
import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import dan200.computercraft.shared.util.WaterloggableHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static dan200.computercraft.shared.util.WaterloggableHelpers.getFluidStateForPlacement;

public class ScrollerBlock extends DirectionalBlock implements EntityBlock, SimpleWaterloggedBlock  {
    public ScrollerBlock() {
        super(BlockBehaviour.Properties.of(Material.METAL).strength(1.0f).sound(SoundType.METAL));
        registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.NORTH)
                .setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.LOCKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.FACING, BlockStateProperties.WATERLOGGED, BlockStateProperties.LOCKED);
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new ScrollerBlockEntity(pos, state);
    }

    @Override
    public @NotNull FluidState getFluidState(@Nonnull BlockState state)
    {
        return WaterloggableHelpers.getFluidState(state);
    }

    @Override
    public @NotNull BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction side, @Nonnull BlockState otherState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos otherPos ) {
        WaterloggableHelpers.updateShape( state, world, pos );
        return state;
    }

    @Override
    public @NotNull VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(FACING)) {
            case SOUTH -> newBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.125f);
            case NORTH -> newBox(0.0f, 0.0f, 1 - 0.125f, 1.0f, 1.0f, 1.0f);
            case WEST -> newBox(1 - 0.125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST -> newBox(0.0f, 0.0f, 0.0f, 0.125f, 1.0f, 1.0f);
            case UP -> newBox(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
            case DOWN -> newBox(0.0f, 1 - 0.125f, 0.0f, 1.0f, 1.0f, 1.0f);
            default -> newBox(0, 0, 0, 1, 1, 1);
        };
    }

    private static VoxelShape newBox(float p1, float p2, float p3, float p4, float p5, float p6) {
        return Block.box(p1*16,p2*16,p3*16,p4*16,p5*16,p6*16);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
        return CCCRegister.getBlockEntityType("scroller_block") == type ? ScrollerBlockEntity::tick : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, ctx.getClickedFace())
                .setValue(BlockStateProperties.WATERLOGGED, getFluidStateForPlacement(ctx))
                .setValue(BlockStateProperties.LOCKED, false);
    }
}
