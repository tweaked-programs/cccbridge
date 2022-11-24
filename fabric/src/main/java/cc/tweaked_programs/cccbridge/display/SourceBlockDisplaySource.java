package cc.tweaked_programs.cccbridge.display;

import cc.tweaked_programs.cccbridge.blockEntity.SourceBlockEntity;
import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.source.DisplaySource;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import com.simibubi.create.foundation.utility.Lang;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.text.MutableText;

import java.util.LinkedList;
import java.util.List;

public class SourceBlockDisplaySource extends DisplaySource {
    @Override
    public List<MutableText> provideText(DisplayLinkContext context, DisplayTargetStats stats) {
        BlockEntity block = context.getSourceTE();
        if (!(block instanceof SourceBlockEntity sourceBlock))
            return EMPTY;
        sourceBlock.setSize(stats.maxColumns(), stats.maxRows());

        List<String> data = sourceBlock.getContent();
        if (data == null)
            return EMPTY;

        List<MutableText> content = new LinkedList<>();
        for (String line : data)
            content.add(Lang.translateDirect("").append(line));

        return content;
    }
}