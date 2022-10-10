package cc.tweaked_programs.cccbridge;

import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class CCCSoundEvents {
    public static final Map<Identifier, SoundEvent> entries = new HashMap<>();

    public static final SoundEvent
            CAGE_LOCK = create("cage_lock"),
            CAGE_UNLOCK = create("cage_unlock");

    public static SoundEvent create(String path) {
        Identifier id = new Identifier(CCCBridge.MOD_ID, path);
        SoundEvent sound = new SoundEvent(id);
        entries.put(id, sound);
        return sound;
    }

    public static void init() {
        for (Identifier id : entries.keySet())
            Registry.register(Registry.SOUND_EVENT, id, entries.get(id));
    }
}
