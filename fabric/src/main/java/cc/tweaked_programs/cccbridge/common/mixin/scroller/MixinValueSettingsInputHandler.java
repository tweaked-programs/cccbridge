package cc.tweaked_programs.cccbridge.common.mixin.scroller;

import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.ScrollerBlockEntity;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueSettingsInputHandler;
import com.simibubi.create.foundation.utility.AdventureUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ValueSettingsInputHandler.class)
public class MixinValueSettingsInputHandler {
    private static boolean isPhysicalPlayer(Player player) {
        return player != null && !player.isSpectator();
    }

    @Redirect(method = "canInteract", at = @At(value = "INVOKE", target = "Lcom/simibubi/create/foundation/utility/AdventureUtil;isAdventure(Lnet/minecraft/world/entity/player/Player;)Z"), remap = false)
    private static boolean cccbridge$canInteract$redirectIsAdventure(Player player) {
        Level level = player.level();
        HitResult hitResult = player.pick(5, 1, false);

        if (hitResult instanceof BlockHitResult blockHit) {
            BlockEntity blockEntity = level.getBlockEntity(blockHit.getBlockPos());

            if (blockEntity instanceof ScrollerBlockEntity)
                return !isPhysicalPlayer(player);
        }

        return !AdventureUtil.isAdventure(player);
    }
}
