package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.blockEntity.ScrollerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ScrollerBlock extends DirectionalBlock implements EntityBlock {
    public ScrollerBlock() {
        super(BlockBehaviour.Properties.of(Material.WOOD).strength(1.0f).sound(SoundType.WOOD));
        registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.FACING, Direction.NORTH)
                //.setValue(BlockStateProperties.WATERLOGGED, false)
                .setValue(BlockStateProperties.LOCKED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.FACING, /*BlockStateProperties.WATERLOGGED,*/ BlockStateProperties.LOCKED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ScrollerBlockEntity(pos, state);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter view, BlockPos pos, CollisionContext ctx) {
        switch(state.getValue(FACING)) {
            case SOUTH:
                return newBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.125f);
            case NORTH:
                return newBox(0.0f, 0.0f, 1-0.125f, 1.0f, 1.0f, 1.0f);
            case WEST:
                return newBox(1-0.125f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
            case EAST:
                return newBox(0.0f, 0.0f, 0.0f, 0.125f, 1.0f, 1.0f);
            case UP:
                return newBox(0.0f, 0.0f, 0.0f, 1.0f, 0.125f, 1.0f);
            case DOWN:
                return newBox(0.0f, 1-0.125f, 0.0f, 1.0f, 1.0f, 1.0f);
            default:
                return newBox(0,0,0,1,1,1);
        }
    }

    private static VoxelShape newBox(float p1, float p2, float p3, float p4, float p5, float p6) {
        return Block.box(p1*16,p2*16,p3*16,p4*16,p5*16,p6*16);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return BlockRegister.SCROLLER_BLOCK_ENTITY.get() == type ? ScrollerBlockEntity::tick : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.FACING, ctx.getClickedFace())
                //.setValue(BlockStateProperties.WATERLOGGED,ctx.getLevel().getFluidState(ctx.getBlockPos()).getFluid() == Fluids.WATER)
                .setValue(BlockStateProperties.LOCKED, false);
    }
}
