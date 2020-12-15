package broccolai.tags.model.tag;

import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class Tag {

    private final int id;
    private final boolean secret;
    private final Component component;
    private final String reason;

    public Tag(final int id, final boolean secret, final Component component, final String reason) {
        this.id = id;
        this.secret = secret;
        this.component = component;
        this.reason = reason;
    }

    public int id() {
        return this.id;
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
