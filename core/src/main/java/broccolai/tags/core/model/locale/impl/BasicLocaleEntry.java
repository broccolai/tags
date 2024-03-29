package broccolai.tags.core.model.locale.impl;

import broccolai.tags.core.model.locale.LocaleEntry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class BasicLocaleEntry implements LocaleEntry {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final String serialised;

    public BasicLocaleEntry(final @NonNull String serialised) {
        this.serialised = serialised;
    }

    @Override
    public Component asComponent(final TagResolver... templates) {
        return MINI.deserialize(this.serialised, templates);
    }

    @Override
    public String toString() {
        return this.serialised;
    }

}
