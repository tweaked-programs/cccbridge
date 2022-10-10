package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public class ScrollerBlock extends FacingBlock implements BlockEntityProvider, Waterloggable {
    public ScrollerBlock() {
        super(FabricBlockSettings.of(Material.WOOD).strength(1.0f).sounds(BlockSoundGroup.WOOD));
        setDefaultState(this.stateManager.getDefaultState()
                .with(Properties.FACING, Direction.NORTH)
                .with(Properties.WATERLOGGED, false)
                .with(Properties.LOCKED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.FACING, Properties.WATERLOGGED, Properties.LOCKED);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollerBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext ctx) {
        Direction dir = state.get(FACING);
        switch(dir) {
            case SOUTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.125f);
            case NORTH:
                return VoxelShapes.cuboid(0.0f, 0.0f, 1-0.125f, 1.0f, 1.0f, 1.0f);
            case WEST:
                return VoxelShapes.cuboid(1-0.125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 0.125f, 1.0f, 1.0f);
            case UP:
                return VoxelShapes.cuboid(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
            case DOWN:
                return VoxelShapes.cuboid(0.0f, 1-0.125f, 0.0f, 1.0f, 1.0f, 1.0f);
            default:
                return VoxelShapes.fullCube();
        }
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.get(Properties.WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(state);
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
        if (state.get(Properties.WATERLOGGED))
            world.createAndScheduleFluidTick(pos, Fluids.WATER, Fluids.WATER.getTickRate(world));

        return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockRegister.getBlockEntityType("scroller_block") == type ? ScrollerBlockEntity::tick : null;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState()
                .with(Properties.FACING, ctx.getSide())
                .with(Properties.WATERLOGGED,ctx.getWorld().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .with(Properties.LOCKED, false);
    }
}
