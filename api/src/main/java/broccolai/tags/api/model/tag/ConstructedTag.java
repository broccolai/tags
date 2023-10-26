package broccolai.tags.api.model.tag;

import broccolai.tags.api.model.Permissible;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public record ConstructedTag(
        int id,
        @NonNull String name,
        boolean secret,
        @NonNull Component component,
        @NonNull String reason,
        @NonNull TagDisplayInformation displayInformation
) implements Permissible {

    @Override
    public @NonNull String permission() {
        return "tags.tag." + this.id();
    }

}
