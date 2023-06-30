package cc.tweaked_programs.cccbridge.display;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import cc.tweaked_programs.cccbridge.Misc;
import cc.tweaked_programs.cccbridge.peripherals.SourceBlockPeripheral;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.source.DisplaySource;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

public class SourceBlockDisplaySource extends DisplaySource {
    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity block = context.getSourceBlockEntity();

        if (!(block instanceof SourceBlockEntity sourceBlock))
            return EMPTY;
        if (!(sourceBlock.getPeripheral(null) instanceof SourceBlockPeripheral peripheral))
            return EMPTY;

        sourceBlock.setSize(stats.maxColumns(), stats.maxRows());

        List<String> data = peripheral.getContent();
        if (data == null)
            return EMPTY;

        List<MutableComponent> content = new LinkedList<>();
        for (String line : data)
            content.add(Misc.toMCTxt(line));

        return content;
    }

    @Override
    public int getPassiveRefreshTicks() {
        return 20;
    }
}