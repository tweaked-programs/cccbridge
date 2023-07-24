package cc.tweaked_programs.cccbridge.common.minecraft.block;

import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.TargetBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class TargetBlock extends Block implements EntityBlock {
    public static final Properties TARGET_BLOCK_PROPERTIES = FabricBlockSettings.create().strength(3.0f).sound(SoundType.METAL);

    public TargetBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new TargetBlockEntity(pos, state);
    }
}
