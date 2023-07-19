package cc.tweaked_programs.cccbridge.assistance.misc;

public class RustyMovement {
    private static float easeOutBounce(float x) {
        float n1 = 7.5625F;
        float d1 = 2.75F;

        if (x < 1F / d1) {
            return n1 * x * x;
        } else if (x < 2F / d1) {
            return n1 * (x -= 1.5F / d1) * x + 0.75F;
        } else if (x < 2.5F / d1) {
            return n1 * (x -= 2.25F / d1) * x + 0.9375F;
        } else {
            return n1 * (x -= 2.625F / d1) * x + 0.984375F;
        }
    }
    private static float easeInOutBounce(float x) {
        return x < 0.5
                ? (1 - easeOutBounce(1 - 2 * x)) / 2
                : (1 + easeOutBounce(2 * x - 1)) / 2;
    }

    public static float updateMovement(float start, float destination, double progress) {
        float interpolatedValue = easeInOutBounce((float)progress);

        float newPos = (destination-start) * interpolatedValue;
        return start + newPos;
    }
}
