package broccolai.tags.api.model.tag;

import org.checkerframework.checker.nullness.qual.NonNull;

public final class TagDisplayInformation {
    private final @NonNull String material;
    private final int customModelData;

    public TagDisplayInformation(
            @NonNull String material,
            int customModelData
    ) {
        this.material = material;
        this.customModelData = customModelData;
    }

    public @NonNull String material() {
        return this.material;
    }

    public int customModelData() {
        return this.customModelData;
    }

}
