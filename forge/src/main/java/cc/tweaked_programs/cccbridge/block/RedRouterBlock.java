package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.CCCBridge;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Material;

public class RedRouterBlock extends HorizontalDirectionalBlock implements EntityBlock {
    public RedRouterBlock() {
        super(Properties.of(Material.STONE).strength(1.3f).requiresCorrectToolForDrops().sound(SoundType.STONE));
        registerDefaultState(this.stateDefinition.any().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager.add(BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new RedRouterBlockEntity(pos, state);
    }

    public boolean isSignalSource(BlockState state) {
        return true;
    }

    public int getSignal(BlockState state, BlockGetter world, BlockPos pos, Direction dir) {
        BlockEntity block = world.getBlockEntity(pos);
        if (!(block instanceof RedRouterBlockEntity redrouter))
            return 0;
        return redrouter.getPower(dir);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return BlockRegister.REDROUTER_BLOCK_ENTITY.get() == type ? RedRouterBlockEntity::tick : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite());
    }
}