package cc.tweaked_programs.cccbridge.common.mixin;

import cc.tweaked_programs.cccbridge.common.minecraft.blockEntity.ScrollerBlockEntity;
import com.simibubi.create.foundation.utility.AdventureUtil;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AdventureUtil.class)
public abstract class MixinAdventureUtil {
    @Inject(method = "isAdventure", at = @At("HEAD"), cancellable = true, remap = false)
    private static void cccbridge$isAdventure(Player player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isSpectator())
            return;

        Level level = player.level();
        HitResult hitResult = player.pick(5, 1, false);

        if (hitResult instanceof BlockHitResult blockHit) {
            BlockEntity blockEntity = level.getBlockEntity(blockHit.getBlockPos());

            if (blockEntity instanceof ScrollerBlockEntity)
                cir.setReturnValue(false);
        }
    }
}
