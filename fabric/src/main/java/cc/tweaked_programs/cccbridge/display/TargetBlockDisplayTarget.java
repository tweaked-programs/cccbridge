package cc.tweaked_programs.cccbridge.display;

import cc.tweaked_programs.cccbridge.blockEntity.TargetBlockEntity;
import com.simibubi.create.content.logistics.block.display.DisplayLinkContext;
import com.simibubi.create.content.logistics.block.display.target.DisplayBoardTarget;
import com.simibubi.create.content.logistics.block.display.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

public class TargetBlockDisplayTarget extends DisplayBoardTarget {
    @Override
    public void acceptFlapText(int line, List<List<MutableComponent>> text, DisplayLinkContext context) {
        BlockEntity block = context.getTargetTE();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return;

        List<String> content = new LinkedList<>();
        for (List<MutableComponent> c : text) {
            String parts = "";
            for (MutableComponent sLine : c)
                parts = parts + sLine.getString() + " ";
            content.add(parts);
        }

        targetBlock.updateContent(line, content);
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        BlockEntity block = context.getTargetTE();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return new DisplayTargetStats(24, 1, this);

        return new DisplayTargetStats(24, targetBlock.getWidth(), this);
    }
}