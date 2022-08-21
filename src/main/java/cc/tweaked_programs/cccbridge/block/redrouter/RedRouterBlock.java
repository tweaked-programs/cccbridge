package cc.tweaked_programs.cccbridge.block.redrouter;

import cc.tweaked_programs.cccbridge.CCCBridge;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RedRouterBlock extends HorizontalFacingBlock implements BlockEntityProvider {
    public RedRouterBlock() {
        super(FabricBlockSettings.of(Material.STONE).strength(1.3f).sounds(BlockSoundGroup.STONE));
        setDefaultState(this.stateManager.getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager) {
        stateManager.add(Properties.HORIZONTAL_FACING);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedRouterBlockEntity(pos, state);
    }

    public boolean emitsRedstonePower(BlockState state) {
        return true;
    }

    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockEntity block = world.getBlockEntity(pos);
        if (!(block instanceof RedRouterBlockEntity redrouter))
            return 0;
        return redrouter.getPower(dir);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return CCCBridge.REDROUTER_BLOCK_ENTITY == type ? RedRouterBlockEntity::tick : null;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return (BlockState)this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
}