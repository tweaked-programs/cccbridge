package cc.tweaked_programs.cccbridge;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;

import java.util.HashMap;
import java.util.Map;

public class CCCSoundEvents {
    public static final Map<ResourceLocation, SoundEvent> entries = new HashMap<>();

    public static final SoundEvent
            CAGE_LOCK = create("cage_lock"),
            CAGE_UNLOCK = create("cage_unlock");

    public static SoundEvent create(String path) {
        ResourceLocation id = new ResourceLocation(CCCBridge.MOD_ID, path);
        SoundEvent sound = new SoundEvent(id);
        entries.put(id, sound);
        return sound;
    }

    public static void init(DeferredRegister<SoundEvent> SOUNDS) {
        for (ResourceLocation id : entries.keySet())
            SOUNDS.register(id.getPath(), () -> entries.get(id));
    }
}
