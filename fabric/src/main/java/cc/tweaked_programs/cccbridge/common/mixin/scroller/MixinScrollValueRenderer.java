package cc.tweaked_programs.cccbridge.common.mixin.scroller;

import cc.tweaked_programs.cccbridge.common.minecraft.block.ScrollerBlock;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueBehaviour;
import com.simibubi.create.foundation.blockEntity.behaviour.scrollValue.ScrollValueRenderer;
import com.simibubi.create.foundation.utility.AdventureUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ScrollValueRenderer.class)
public abstract class MixinScrollValueRenderer {
    private static boolean isPhysicalPlayer(Player player) {
        return player != null && !player.isSpectator();
    }

    @Redirect(method = "addBox", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/AdventureUtil;isAdventure(Lnet/minecraft/world/entity/player/Player;)Z"), remap = false)
    private static boolean cccbridge$addBox$redirectIsAdventure(Player player, ClientLevel world, BlockPos pos, Direction face, ScrollValueBehaviour behaviour, boolean highlight) {
         if (world.getBlockState(pos).getBlock() instanceof ScrollerBlock)
             return isPhysicalPlayer(Minecraft.getInstance().player);

         return AdventureUtil.isAdventure(Minecraft.getInstance().player);
    }
}
