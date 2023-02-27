package cc.tweaked_programs.cccbridge;

public class Misc {
    private static String dot = Character.toString(183);

    /***
     * Replaces chars in given line, so they can be displayed in CC.
     * @param line The raw line.
     * @return The edited line, compatible with CC.
     */
    public static String CreateToComputersCharset(String line) {
        line = line.replaceAll("\u2588", "=");
        line = line.replaceAll("\u2592", "-");
        line = line.replaceAll("\u2591", dot);

        return line;
    }
}
