package broccolai.tags.api.model.tag;

import broccolai.tags.api.model.Permissible;
import net.kyori.adventure.text.Component;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public final class ConstructedTag implements Permissible {

    private final int id;
    private final String name;
    private final boolean secret;
    private final @NonNull Component component;
    private final @NonNull String reason;
    private final @Nullable TagDisplayInformation displayInformation;

    public ConstructedTag(
            final int id,
            final @NonNull String name,
            final boolean secret,
            final @NonNull Component component,
            final @NonNull String reason,
            final @Nullable TagDisplayInformation displayInformation
    ) {
        this.id = id;
        this.name = name;
        this.secret = secret;
        this.component = component;
        this.reason = reason;
        this.displayInformation = displayInformation;
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

    @Override
    public @NonNull String permission() {
        return "tags.tag." + this.id();
    }

    public @Nullable TagDisplayInformation displayInformation() {
        return this.displayInformation;
    }

}
