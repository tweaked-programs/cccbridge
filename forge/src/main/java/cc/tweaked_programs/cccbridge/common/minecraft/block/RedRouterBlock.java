package cc.tweaked_programs.cccbridge.common.minecraft.block;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.assistance.Randomness;
import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.RedRouterBlockEntity;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.NotNull;

public class RedRouterBlock extends HorizontalDirectionalBlock implements EntityBlock, IWrenchable {
    public static final Properties REDROUTER_BLOCK_PROPERTIES = BlockBehaviour.Properties.of().strength(1.3f).sound(SoundType.STONE).noOcclusion().isRedstoneConductor((state, view, pos) -> false);
    public static final int FACE_AMOUNT = 16;
    public static final IntegerProperty FACE = IntegerProperty.create("face", 0, FACE_AMOUNT);
    public RedRouterBlock(Properties properties) {
        super(properties);
        registerDefaultState(this.stateDefinition.any()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH)
                .setValue(FACE, 0)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateManager) {
        stateManager
                .add(BlockStateProperties.HORIZONTAL_FACING)
                .add(FACE);
    }

    @Override
    public void neighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos fromPos, boolean pIsMoving) {
        if (!level.isClientSide) {
            BlockEntity be = level.getBlockEntity(pos);
            if (!(be instanceof RedRouterBlockEntity rbe))
                return;
            RedRouterBlockEntity.updateInputs(level, pos, rbe);
            rbe.setChanged();
        }
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new RedRouterBlockEntity(pos, state);
    }

    @Override
    public boolean isSignalSource(@NotNull BlockState state) {
        return true;
    }

    @Override
    public int getDirectSignal(@NotNull BlockState state, BlockGetter world, @NotNull BlockPos pos, @NotNull Direction direction) {
        BlockEntity block = world.getBlockEntity(pos);
        if (!(block instanceof RedRouterBlockEntity redrouter))
            return 0;
        return redrouter.getPower(direction);
    }

    @Override
    public int getSignal(@NotNull BlockState state, @NotNull BlockGetter level, @NotNull BlockPos pos, @NotNull Direction direction) {
        return getDirectSignal(state, level, pos, direction);
    }

    @Override
    public InteractionResult onWrenched(BlockState state, UseOnContext context) {
        BlockEntity tileentity = context.getLevel().getBlockEntity(context.getClickedPos());
        if (!(tileentity instanceof RedRouterBlockEntity redrouter))
            return InteractionResult.FAIL;

        if (context.getClickedFace().get3DDataValue() < 2) {
            int north = redrouter.getPower(Direction.NORTH);
            int east = redrouter.getPower(Direction.EAST);
            int south = redrouter.getPower(Direction.SOUTH);
            int west = redrouter.getPower(Direction.WEST);

            redrouter.setPower("north", west);
            redrouter.setPower("east", north);
            redrouter.setPower("south", east);
            redrouter.setPower("west", south);
        }

        return IWrenchable.super.onWrenched(state, context);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
        return CCCRegistries.REDROUTER_BLOCK_ENTITY.get() == type ? RedRouterBlockEntity::tick : null;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return this.defaultBlockState()
                .setValue(BlockStateProperties.HORIZONTAL_FACING, ctx.getHorizontalDirection().getOpposite())
                .setValue(FACE, (ctx.getLevel().isClientSide) ? 0 : Randomness.randomFace());
    }
}