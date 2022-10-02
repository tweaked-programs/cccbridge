package cc.tweaked_programs.cccbridge.display;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.source.DisplaySource;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

public class SourceBlockDisplaySource extends DisplaySource {
    @Override
    public List<MutableComponent> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity block = context.getSourceTE();
        if (!(block instanceof SourceBlockEntity sourceBlock))
            return EMPTY;
        sourceBlock.setSize(stats.maxColumns(), stats.maxRows());

        List<String> data = sourceBlock.getContent();
        if (data == null)
            return EMPTY;

        List<MutableComponent> content = new LinkedList<>();
        for (String line : data) {
            content.add(new TextComponent("").append(line));
        }

        return content;
    }
}