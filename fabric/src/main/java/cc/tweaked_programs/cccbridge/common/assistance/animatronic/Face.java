package cc.tweaked_programs.cccbridge.common.assistance.animatronic;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Face {
    NORMAL("normal"),
    HAPPY("happy"),
    QUESTION("question"),
    SAD("sad");

    private final String id;

    Face(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static @Nullable Face contains(String kind) {
        return Arrays.stream(Face.values())
                .filter(face -> face.getId().equalsIgnoreCase(kind))
                .findFirst()
                .orElse(null);
    }

    public static String availableOptions() {
        String values = Arrays.stream(Face.values())
                .map(Face::getId)
                .collect(Collectors.joining(", "));

        return "{ " + values + " }";
    }
}
