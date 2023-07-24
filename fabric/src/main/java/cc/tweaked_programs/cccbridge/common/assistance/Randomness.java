package cc.tweaked_programs.cccbridge.common.assistance;

import cc.tweaked_programs.cccbridge.client.modloader.CCCBridgeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;

import java.util.Random;

import static cc.tweaked_programs.cccbridge.common.minecraft.block.RedRouterBlock.FACE_AMOUNT;

public class Randomness {
    private static final Random random = new Random();

    @Environment(EnvType.CLIENT)
    public static int lightFlickering() {
        if (CCCBridgeClient.ONFIG.FLICKERING)
            return ((int)(LightTexture.FULL_BRIGHT - (Math.random()*35) - (random.nextInt(0,45) == 1 ? 50 : 0)));
        else
            return LightTexture.FULL_BRIGHT;
    }

    @Environment(EnvType.CLIENT)
    public static boolean rareCreepiness() {
        return (random.nextInt(0, 96969) == 1337);
    } // Please for the love of god let this never happen to anyone

    public static int randomFace() {
        return random.nextInt(0, FACE_AMOUNT)+1;
    }
}
