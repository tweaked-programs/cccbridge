package cc.tweaked_programs.cccbridge.block.redrouter;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;

public class RedRouterBlock extends RedstoneBlock implements BlockEntityProvider {
    public RedRouterBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(2.8f).requiresTool().sounds(BlockSoundGroup.COPPER));
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new RedRouterBlockEntity(pos, state);
    }

    @Override
    public int getWeakRedstonePower(BlockState state, BlockView world, BlockPos pos, Direction dir) {
        BlockEntity block = world.getBlockEntity(pos);
        if (!(block instanceof RedRouterBlockEntity redrouter))
            return 0;
        return redrouter.getPoweredSide(dir);
    }
}