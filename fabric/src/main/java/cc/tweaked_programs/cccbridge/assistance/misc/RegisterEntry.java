package cc.tweaked_programs.cccbridge.assistance.misc;

public record RegisterEntry(String id, Object entry, TYPE type) {
    public enum TYPE {
        BLOCK(),
        ITEM(),
        BLOCK_ENTITY(),
        ENTITY(),
        SOUND_EVENT(),
        PAINTING_VARIANT();
    }
}
