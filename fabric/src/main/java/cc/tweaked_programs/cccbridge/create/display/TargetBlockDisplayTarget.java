package cc.tweaked_programs.cccbridge.create.display;

import cc.tweaked_programs.cccbridge.assistance.misc.CharsetManipulator;
import cc.tweaked_programs.cccbridge.minecraft.blockEntity.TargetBlockEntity;
import com.simibubi.create.content.redstone.displayLink.DisplayLinkContext;
import com.simibubi.create.content.redstone.displayLink.target.DisplayBoardTarget;
import com.simibubi.create.content.redstone.displayLink.target.DisplayTargetStats;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.List;

public class TargetBlockDisplayTarget extends DisplayBoardTarget {
    @Override
    public void acceptFlapText(int line, List<List<MutableComponent>> text, DisplayLinkContext context) {
        BlockEntity block = context.getTargetBlockEntity();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return;

        List<String> content = new LinkedList<>();
        for (List<MutableComponent> c : text) {
            StringBuilder parts = new StringBuilder();
            for (MutableComponent sLine : c)
                parts.append(sLine.getString()).append(" ");
            content.add(CharsetManipulator.toCCTxt(parts.toString()));
        }

        targetBlock.updateContent(line, content);
    }

    @Override
    public DisplayTargetStats provideStats(DisplayLinkContext context) {
        BlockEntity block = context.getTargetBlockEntity();
        if (!(block instanceof TargetBlockEntity targetBlock))
            return new DisplayTargetStats(24, 1, this);

        return new DisplayTargetStats(24, targetBlock.getWidth(), this);
    }
}