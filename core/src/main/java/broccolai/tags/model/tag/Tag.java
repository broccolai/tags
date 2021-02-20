package broccolai.tags.model.tag;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Tag {

    private final int id;
    private final String name;
    private final boolean secret;
    private final @NonNull Component component;
    private final @NonNull String reason;

    public Tag(
            final int id,
            final @NonNull String name,
            final boolean secret,
            final @NonNull Component component,
            final @NonNull String reason
    ) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.component = component;
        this.reason = reason;
    }

    public int id() {
        return this.id;
    }

    public @NonNull String name() {
        return this.name;
    }

    public boolean secret() {
        return this.secret;
    }

    public @NonNull Component component() {
        return this.component;
    }

    public @NonNull String reason() {
        return this.reason;
    }

}
