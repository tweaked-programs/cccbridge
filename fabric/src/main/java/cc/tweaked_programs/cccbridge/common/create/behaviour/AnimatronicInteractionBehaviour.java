package cc.tweaked_programs.cccbridge.common.create.behaviour;

import cc.tweaked_programs.cccbridge.common.minecraft.block.AnimatronicBlock;
import com.simibubi.create.AllItems;
import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.api.behaviour.interaction.MovingInteractionBehaviour;
import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.trains.entity.CarriageContraption;
import com.simibubi.create.content.trains.entity.CarriageContraptionEntity;
import com.simibubi.create.content.trains.entity.Train;
import com.simibubi.create.content.trains.schedule.Schedule;
import com.simibubi.create.content.trains.schedule.ScheduleItem;
import com.simibubi.create.foundation.advancement.AllAdvancements;
import net.createmod.catnip.data.Iterate;
import net.createmod.catnip.lang.Lang;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class AnimatronicInteractionBehaviour extends MovingInteractionBehaviour {
    @Override
    public boolean handlePlayerInteraction(Player player, InteractionHand activeHand, BlockPos localPos, AbstractContraptionEntity contraptionEntity) {
            ItemStack itemInHand = player.getItemInHand(activeHand);

            if (!(contraptionEntity instanceof CarriageContraptionEntity carriageEntity))
                return false;
            if (activeHand == InteractionHand.OFF_HAND)
                return false;
            Contraption contraption = carriageEntity.getContraption();
            if (!(contraption instanceof CarriageContraption carriageContraption))
                return false;

            Direction assemblyDirection = carriageContraption.getAssemblyDirection();
            for (Direction direction : Iterate.directionsInAxis(assemblyDirection.getAxis())) {
                if (!carriageContraption.inControl(localPos, direction))
                    continue;

                Train train = carriageEntity.getCarriage().train;
                if (train == null)
                    return false;
                if (player.level().isClientSide)
                    return true;

                setHasJobState(localPos, contraptionEntity, false);

                if (train.runtime.getSchedule() != null) {
                    if (train.runtime.paused && !train.runtime.completed) {
                        train.runtime.paused = false;
                        AllSoundEvents.CONFIRM.playOnServer(player.level(), player.blockPosition(), 1, 1);
                        player.displayClientMessage(
                                Lang.builder("create")
                                        .translate("schedule.continued")
                                        .component(),
                                true
                        );
                        return true;
                    }

                    if (!itemInHand.isEmpty()) {
                        AllSoundEvents.DENY.playOnServer(player.level(), player.blockPosition(), 1, 1);
                        player.displayClientMessage(
                                Lang.builder("create")
                                        .translate("schedule.remove_with_empty_hand")
                                        .component(),
                                true
                        );
                        return true;
                    }
                    AllSoundEvents.playItemPickup(player);
                    player.displayClientMessage(
                            Lang.builder("create")
                                    .translate(train.runtime.isAutoSchedule
                                            ? "schedule.auto_removed_from_train"
                                            : "schedule.removed_from_train")
                                    .component(),
                            true
                    );
                    player.setItemInHand(activeHand, train.runtime.returnSchedule());
                    return true;
                }

                if (!AllItems.SCHEDULE.isIn(itemInHand))
                    return true;

                Schedule schedule = ScheduleItem.getSchedule(itemInHand);
                if (schedule == null)
                    return false;

                if (schedule.entries.isEmpty()) {
                    AllSoundEvents.DENY.playOnServer(player.level(), player.blockPosition(), 1, 1);
                    player.displayClientMessage(Lang.builder("create")
                            .translate("schedule.no_stops")
                            .component(), true);
                    return true;
                }

                setHasJobState(localPos, contraptionEntity, true);

                train.runtime.setSchedule(schedule, false);
                AllAdvancements.CONDUCTOR.awardTo(player);
                AllSoundEvents.CONFIRM.playOnServer(player.level(), player.blockPosition(), 1, 1);
                player.displayClientMessage(Lang.builder("create")
                        .translate("schedule.applied_to_train")
                        .component()
                        .withStyle(ChatFormatting.GREEN), true);
                itemInHand.shrink(1);
                player.setItemInHand(activeHand, itemInHand.isEmpty() ? ItemStack.EMPTY : itemInHand);
                return true;
            }

            player.displayClientMessage(Lang.builder("create")
                    .translate("schedule.non_controlling_seat")
                    .component(), true);
            AllSoundEvents.DENY.playOnServer(player.level(), player.blockPosition(), 1, 1);
            return true;
    }

    private void setHasJobState(BlockPos localPos, AbstractContraptionEntity contraption, boolean value) {
        StructureTemplate.StructureBlockInfo info = contraption.getContraption().getBlocks().get(localPos);
        if (info != null && info.state().getBlock() instanceof AnimatronicBlock) {

            BlockState newState = info.state().setValue(AnimatronicBlock.IS_DRIVER, value);
            setContraptionBlockData(contraption, localPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
        }
    }
}
