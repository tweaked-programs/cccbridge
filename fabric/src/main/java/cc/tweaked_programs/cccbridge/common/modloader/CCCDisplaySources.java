package cc.tweaked_programs.cccbridge.common.modloader;

import cc.tweaked_programs.cccbridge.common.CCCRegistries;
import cc.tweaked_programs.cccbridge.common.create.display.SourceBlockDisplaySource;
import com.simibubi.create.api.behaviour.display.DisplaySource;
import com.tterrag.registrate.util.entry.RegistryEntry;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

import static cc.tweaked_programs.cccbridge.common.modloader.CCCBridge.REGISTRATE;

public class CCCDisplaySources {
    public static RegistryEntry<SourceBlockDisplaySource> SOURCE_BLOCK = simple("source_block_display_source", SourceBlockDisplaySource::new, CCCRegistries.SOURCE_BLOCK.getBlock());

    private static <T extends DisplaySource> RegistryEntry<T> simple(String name, Supplier<T> supplier, Block associatedBlock) {
        return REGISTRATE.displaySource(name, supplier).associate(associatedBlock).register();
    }

    public static void register() {}
}
