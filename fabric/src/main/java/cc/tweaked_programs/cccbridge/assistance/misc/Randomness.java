package cc.tweaked_programs.cccbridge.assistance.misc;

import cc.tweaked_programs.cccbridge.assistance.Config;
import net.minecraft.client.renderer.LightTexture;

import java.util.Random;

import static cc.tweaked_programs.cccbridge.minecraft.block.RedRouterBlock.FACE_AMOUNT;

public class Randomness {
    private static final Random random = new Random();

    public static int lightFlickering() {
        if (Config.FLICKERING)
            return ((int)(LightTexture.FULL_BRIGHT - (Math.random()*40) - (random.nextInt(0,22) == 1 ? 100 : 0)));
        else
            return LightTexture.FULL_BRIGHT;
    }

    public static boolean rareCreepiness() {
        return (random.nextInt(0, 96969) == 1337);
    } // Please for the love of god let this never happen to anyone

    public static int randomFace() {
        return random.nextInt(0, FACE_AMOUNT)+1;
    }
}
