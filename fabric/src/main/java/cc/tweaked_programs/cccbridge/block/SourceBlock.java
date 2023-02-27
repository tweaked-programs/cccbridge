package cc.tweaked_programs.cccbridge.block;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;

public class SourceBlock extends Block implements BlockEntityType.BlockEntitySupplier {
    public SourceBlock() {
        super(FabricBlockSettings.of(Material.METAL).strength(2.0f).sounds(SoundType.COPPER));
    }

    @Override
    public BlockEntity create(BlockPos pos, BlockState state) {
        return new SourceBlockEntity(pos, state);
    }
}