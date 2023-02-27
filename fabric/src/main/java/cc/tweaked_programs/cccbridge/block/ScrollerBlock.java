package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import dan200.computercraft.shared.util.WaterloggableHelpers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nonnull;

import static cc.tweaked_programs.cccbridge.BlockRegister.newBox;

public class ScrollerBlock extends DirectionalBlock implements EntityBlock, SimpleWaterloggedBlock {
    public ScrollerBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(1.0f).sound(SoundType.WOOD));
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
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollerBlockEntity(pos, state);
    }

    @Override
    public FluidState getFluidState(@Nonnull BlockState state)
    {
        return WaterloggableHelpers.getFluidState(state);
    }

    @Override
    public BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction side, @Nonnull BlockState otherState, @Nonnull LevelAccessor world, @Nonnull BlockPos pos, @Nonnull BlockPos otherPos ) {
        WaterloggableHelpers.updateShape(state, world, pos);
        return state;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ctx) {
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

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return BlockRegister.getBlockEntityType("scroller_block") == type ? ScrollerBlockEntity::tick : null;
    }

    public static boolean getFluidStateForPlacement(BlockPlaceContext context) {
        return context.getLevel().getFluidState(context.getClickedPos()).getType() == Fluids.WATER;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, ctx.getClickedFace())
                .setValue(BlockStateProperties.WATERLOGGED, getFluidStateForPlacement(ctx))
                .setValue(BlockStateProperties.LOCKED, false);
    }
}
