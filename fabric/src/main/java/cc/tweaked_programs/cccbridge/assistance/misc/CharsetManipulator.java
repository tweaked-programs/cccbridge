package cc.tweaked_programs.cccbridge.assistance.misc;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CharsetManipulator {
    private static final char[] charsetMC = {
            // Lower CP437 characters
            0x0020, 0x263A, 0x263B, 0x2665, 0x2666, 0x2663, 0x2660, 0x25CF, 0x25CB, 0x0020, 0x0020, 0x2642, 0x2640, 0x0020, 0x266A, 0x266C,
            0x25B6, 0x25C0, 0x2195, 0x203C, 0x00B6, 0x2591, 0x25AC, 0x21A8, 0x2B06, 0x2B07, 0x27A1, 0x2B05, 0x221F, 0x29FA, 0x25B2, 0x25BC,
            // ASCII characters
            0x0020, 0x0021, 0x0022, 0x0023, 0x0024, 0x0025, 0x0026, 0x0027, 0x0028, 0x0029, 0x002A, 0x002B, 0x002C, 0x002D, 0x002E, 0x002F,
            0x0030, 0x0031, 0x0032, 0x0033, 0x0034, 0x0035, 0x0036, 0x0037, 0x0038, 0x0039, 0x003A, 0x003B, 0x003C, 0x003D, 0x003E, 0x003F,
            0x0040, 0x0041, 0x0042, 0x0043, 0x0044, 0x0045, 0x0046, 0x0047, 0x0048, 0x0049, 0x004A, 0x004B, 0x004C, 0x004D, 0x004E, 0x004F,
            0x0050, 0x0051, 0x0052, 0x0053, 0x0054, 0x0055, 0x0056, 0x0057, 0x0058, 0x0059, 0x005A, 0x005B, 0x005C, 0x005D, 0x005E, 0x005F,
            0x0060, 0x0061, 0x0062, 0x0063, 0x0064, 0x0065, 0x0066, 0x0067, 0x0068, 0x0069, 0x006A, 0x006B, 0x006C, 0x006D, 0x006E, 0x006F,
            0x0070, 0x0071, 0x0072, 0x0073, 0x0074, 0x0075, 0x0076, 0x0077, 0x0078, 0x0079, 0x007A, 0x007B, 0x007C, 0x007D, 0x007E, 0x2592,
            // Drawing chars
            0x2800, 0x2801, 0x2808, 0x2809, 0x2802, 0x2803, 0x280A, 0x280B, 0x2810, 0x2811, 0x2818, 0x2819, 0x2812, 0x2813, 0x281A, 0x281B,
            0x2804, 0x2805, 0x280C, 0x280D, 0x2806, 0x2807, 0x280E, 0x280F, 0x2814, 0x2815, 0x281C, 0x281D, 0x2816, 0x2817, 0x281E, 0x281F,
            // ISO-8859-1 characters
            0x2593, 0x00A1, 0x00A2, 0x00A3, 0x00A4, 0x00A5, 0x00A6, 0x2588, 0x00A8, 0x00A9, 0x00AA, 0x00AB, 0x00AC, 0x00AD, 0x00AE, 0x00AF,
            0x00B0, 0x00B1, 0x00B2, 0x00B3, 0x00B4, 0x00B5, 0x00B6, 0x00B7, 0x00B8, 0x00B9, 0x00BA, 0x00BB, 0x00BC, 0x00BD, 0x00BE, 0x00BF,
            0x00C0, 0x00C1, 0x00C2, 0x00C3, 0x00C4, 0x00C5, 0x00C6, 0x00C7, 0x00C8, 0x00C9, 0x00CA, 0x00CB, 0x00CC, 0x00CD, 0x00CE, 0x00CF,
            0x00D0, 0x00D1, 0x00D2, 0x00D3, 0x00D4, 0x00D5, 0x00D6, 0x00D7, 0x00D8, 0x00D9, 0x00DA, 0x00DB, 0x00DC, 0x00DD, 0x00DE, 0x00DF,
            0x00E0, 0x00E1, 0x00E2, 0x00E3, 0x00E4, 0x00E5, 0x00E6, 0x00E7, 0x00E8, 0x00E9, 0x00EA, 0x00EB, 0x00EC, 0x00ED, 0x00EE, 0x00EF,
            0x00F0, 0x00F1, 0x00F2, 0x00F3, 0x00F4, 0x00F5, 0x00F6, 0x00F7, 0x00F8, 0x00F9, 0x00FA, 0x00FB, 0x00FC, 0x00FD, 0x00FE, 0x00FF
    };

    public static MutableComponent toMCTxt(String line) {
        StringBuilder mcTxt = new StringBuilder();

        for (char c : line.toCharArray()) {
            if (c > 255)
                mcTxt.append('?');
            else if (c == 223) // ß
                mcTxt.append((char)0x1E9E);
            else
                mcTxt.append(charsetMC[c]);
        }

        return Component.literal(mcTxt.toString());
    }

    public static String toCCTxt(String line) {
        StringBuilder ccTxt = new StringBuilder();

        for (char c : line.toCharArray()) {
            char index = findCharIndex(c);

            ccTxt.append((index > 255) ? '?' : index);
        }

        return ccTxt.toString();
    }

    private static char findCharIndex(char c) {
        if (c == ' ') // Because we replace some non-printable chars with a space, we have to add this if-clause here.
            return c;

        for (int i=0; i<charsetMC.length; i++)
            if (charsetMC[i] == c)
                return (char)i;

        return '?';
    }

    private static final Pattern colorPattern = Pattern.compile("§[-a-fA-F\\d](?!.*§[-a-fA-F\\d])");
    public static String[] wrap(String text, int width) {
        if (text == null || text.isEmpty()) {
            return new String[0];
        }

        List<String> lines = new ArrayList<>();
        text = text+' ';

        String initialSpaces = text.replaceAll("^(\\s*).*", "$1");
        text = text.substring(initialSpaces.length());

        String[] sentence = text.split("[\\s]");
        StringBuilder line = new StringBuilder(initialSpaces);
        int exceptions = 0;
        for (String word : sentence) {
            line.append(word).append(" ");

            if (line.length() > width+1) {
                if (word.length() >= 6 || exceptions >= 2) {
                    String compiledLine = line.toString();
                    lines.add(compiledLine);

                    // Begin with color and initial spaces
                    Matcher matcher = colorPattern.matcher(compiledLine);
                    line = new StringBuilder((matcher.find() ? matcher.group() : "") + initialSpaces);
                } else
                    exceptions++;
            }
        }
        if (!line.toString().replaceAll(colorPattern.pattern(), "").trim().isEmpty())
            lines.add(line.toString());

        return lines.toArray(new String[0]);
    }
}
