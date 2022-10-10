package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.BlockRegister;
import cc.tweaked_programs.cccbridge.blockEntity.RedRouterBlockEntity;
import com.simibubi.create.content.contraptions.wrench.IWrenchable;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class RedRouterBlock extends HorizontalFacingBlock implements BlockEntityProvider, IWrenchable {
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
    public ActionResult onWrenched(BlockState state, ItemUsageContext context) {
        BlockEntity tileentity = context.getWorld().getBlockEntity(context.getBlockPos());
        if (!(tileentity instanceof RedRouterBlockEntity redrouter))
            return ActionResult.FAIL;

        if (context.getSide().getId() < 2) {
            int north = redrouter.getPower(Direction.NORTH);
            int east = redrouter.getPower(Direction.EAST);
            int south = redrouter.getPower(Direction.SOUTH);
            int west = redrouter.getPower(Direction.WEST);

            redrouter.setPower("north", west);
            redrouter.setPower("east", north);
            redrouter.setPower("south", east);
            redrouter.setPower("west", south);
        }

        ActionResult result = IWrenchable.super.onWrenched(state, context);
        if (!result.isAccepted())
            return result;

        return result;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
        return BlockRegister.getBlockEntityType("redrouter_block") == type ? RedRouterBlockEntity::tick : null;
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
    }
}