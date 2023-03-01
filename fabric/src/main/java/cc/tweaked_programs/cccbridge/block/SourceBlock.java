package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import org.jetbrains.annotations.Nullable;

public class SourceBlock extends Block implements EntityBlock {
    public SourceBlock() {
        super(Properties.of(Material.METAL).strength(2.0f).sound(SoundType.COPPER));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SourceBlockEntity(pos, state);
    }
}
