package cc.tweaked_programs.cccbridge.minecraft.block;

import cc.tweaked_programs.cccbridge.minecraft.blockEntity.SourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class SourceBlock extends Block implements EntityBlock {
    public static final Properties SOURCE_BLOCK_PROPERTIES = BlockBehaviour.Properties.of(Material.METAL).strength(3.0f).sound(SoundType.METAL);
    public SourceBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SourceBlockEntity(pos, state);
    }
}
