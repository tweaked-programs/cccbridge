package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.create.display.TargetBlockDisplayTarget;
import com.simibubi.create.api.behaviour.display.DisplayTarget;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static cc.tweaked_programs.cccbridge.common.modloader.CCCBridge.REGISTRATE;

public class CCCDisplayTargets {
    public static RegistryEntry<TargetBlockDisplayTarget> TARGET_BLOCK = simple("target_block_display_target", TargetBlockDisplayTarget::new, CCCRegistries.TARGET_BLOCK.getBlock());

    private static <T extends DisplayTarget> RegistryEntry<T> simple(String name, Supplier<T> supplier, Block associatedBlock) {
        return REGISTRATE.displayTarget(name, supplier).associate(associatedBlock).register();
    }

    public static void register() {}
}
