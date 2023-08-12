package cc.tweaked_programs.cccbridge.common.create.behaviour;

import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.ScrollerBlockEntity;
import com.google.common.collect.ImmutableList;
import com.simibubi.create.foundation.blockEntity.behaviour.*;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.BlockHitResult;

import java.util.List;

public class LuaScrollValueBehaviour extends ScrollValueBehaviour {
    private boolean hasMinus;

    public LuaScrollValueBehaviour(ScrollerBlockEntity be, ValueBoxTransform slot) {
        super(Component.translatable("block.cccbridge.scroller_block"), be, slot);

        withCallback(i -> be.fireUpdateValueEvent());
        onlyActiveWhen(() -> (be.getLevel() != null && !(be.getLevel().getBlockState(be.getBlockPos()).getValue(BlockStateProperties.LOCKED))));
        value = 0;
        hasMinus = false;
        between(-15,15);
    }

    public void setValueQuietly(int value) {
        value = Mth.clamp(value, -max, max);
        if (value == this.value)
            return;
        this.value = value;
        blockEntity.setChanged();
        blockEntity.sendData();
    }

    @Override
    public void write(CompoundTag nbt, boolean clientPacket) {
        nbt.putInt("ScrollValue", value);
        nbt.putBoolean("ScrollMinus", hasMinus);
        nbt.putInt("ScrollLimit", max);
        super.write(nbt, clientPacket);
    }

    @Override
    public void read(CompoundTag nbt, boolean clientPacket) {
        value = nbt.getInt("ScrollValue");
        hasMinus = nbt.getBoolean("ScrollMinus");
        int limit = nbt.getInt("ScrollLimit");
        between(-limit, limit);
        super.read(nbt, clientPacket);
    }

    @Override
    public void playFeedbackSound(BlockEntityBehaviour origin) {
        origin.getWorld()
                .playSound(null, origin.getPos(), SoundEvents.COPPER_PLACE, SoundSource.BLOCKS, 0.25f, 2f);
        origin.getWorld()
                .playSound(null, origin.getPos(), SoundEvents.NOTE_BLOCK_BIT.value(), SoundSource.BLOCKS, 0.05f, 1.25f);
    }

    public void between(int limit) {
        between(-limit, limit);
        blockEntity.setChanged();
        blockEntity.sendData();
    }

    public void setHasMinus(boolean state) {
        hasMinus = state;
        blockEntity.setChanged();
        blockEntity.sendData();
    }

    public int getMax() {
        return max;
    }

    public boolean hasMinus() {
        return hasMinus;
    }

    @Override
    public ValueSettingsBoard createBoard(Player player, BlockHitResult hitResult) {
        List<Component> rows;
        if (hasMinus)
            rows = ImmutableList.of(
                Components.translatable("cccbridge.general.unit.scroller.negative"),
                Components.translatable("cccbridge.general.unit.scroller.positive")
            );
        else
            rows = ImmutableList.of(
                    Components.translatable("cccbridge.general.unit.scroller")
            );

        return new ValueSettingsBoard(label, max, 5, rows,
                new ValueSettingsFormatter(ValueSettingsBehaviour.ValueSettings::format));
    }

    @Override
    public void setValueSettings(Player player, ValueSettingsBehaviour.ValueSettings valueSetting, boolean ctrlDown) {
        if (valueSetting.equals(getValueSettings()))
            return;
        setValue(hasMinus && valueSetting.row() == 0 ? -valueSetting.value() : valueSetting.value());
        playFeedbackSound(this);
    }
}
