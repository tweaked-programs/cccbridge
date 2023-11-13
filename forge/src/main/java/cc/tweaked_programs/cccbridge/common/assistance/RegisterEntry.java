package cc.tweaked_programs.cccbridge.common.assistance;

public record RegisterEntry(String id, Object entry, TYPE type) {
    public enum TYPE {
        BLOCK(),
        ITEM(),
        BLOCK_ENTITY(),
        SOUND_EVENT(),
        PAINTING_VARIANT();
    }
}
