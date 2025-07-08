package dev.kleinbox.cccbridge.common.assistance.animatronic;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public enum Transition {
    RUSTY("rusty"),
    NONE("none"),
    LINEAR("linear");

    private final String id;

    Transition(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static @Nullable Transition contains(String kind) {
        return Arrays.stream(Transition.values())
                .filter(transition -> transition.getId().equalsIgnoreCase(kind))
                .findFirst()
                .orElse(null);
    }

    public static String availableOptions() {
        String values = Arrays.stream(Transition.values())
                .map(Transition::getId)
                .collect(Collectors.joining(", "));

        return "{ " + values + " }";
    }
}
