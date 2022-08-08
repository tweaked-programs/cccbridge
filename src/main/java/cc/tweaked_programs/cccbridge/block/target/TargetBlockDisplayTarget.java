package cc.tweaked_programs.cccbridge.block.target;

import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.target.DisplayTarget;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

public class TargetBlockDisplayTarget extends DisplayTarget {
    @Override
    public void acceptText(int line, List<MutableComponent> text, DisplayLinkContext context) {
        BlockEntity block = context.getTargetTE();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return;

        List<String> content = new LinkedList<>();
        for (MutableComponent sLine : text)
            content.add(sLine.getString());

        targetBlock.updateContent(line, content);
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        BlockEntity block = context.getTargetTE();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return new DisplayTargetStats(16, 1, this);

        return new DisplayTargetStats(16, targetBlock.getWidth(), this);
    }
}