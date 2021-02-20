package broccolai.tags.service.tags.impl;

import broccolai.tags.config.Configuration;
import broccolai.tags.config.TagConfiguration;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.TreeMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;

@Singleton
public final class MappedTagsService implements TagsService {

    private static final MiniMessage MINI = MiniMessage.get();

    private final @NonNull Map<Integer, Tag> idToTags = new HashMap<>();
    private final @NonNull Map<String, Tag> nameToTags = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final @NonNull Permission permission;
    private final int defaultId;

    @Inject
    public MappedTagsService(final @NonNull Configuration configuration, final @NonNull Permission permission) {
        this.permission = permission;
        this.defaultId = configuration.defaultTag;

        for (TagConfiguration config : configuration.tags) {
            this.create(config.id, config.name, config.secret, config.component, config.reason);
        }
    }

    @Override
    public @NotNull Tag defaultTag() {
        return this.load(this.defaultId);
    }

    @Override
    public void create(
            final int id,
            final @NonNull String name,
            final boolean secret,
            final @NonNull String componentString,
            final @NonNull String reason
    ) {
        Component component = MINI.parse(componentString);

        Tag tag = new Tag(id, name, secret, component, reason);

        this.idToTags.put(id, tag);
        this.nameToTags.put(name.toLowerCase(), tag);
    }

    @Override
    public @Nullable Tag load(final int id) {
        return this.idToTags.get(id);
    }

    @Override
    public @Nullable Tag load(final @NonNull String name) {
        return this.nameToTags.get(name);
    }

    @Override
    public @NonNull Tag load(final @NonNull TagsUser user) {
        return user.current()
                .map(this::load)
                .filter(tag -> user.owns(this.permission, tag))
                .orElse(this.defaultTag());
    }

    @Override
    public @NonNull Collection<Tag> allTags() {
        return Collections.unmodifiableCollection(this.idToTags.values());
    }

    @Override
    public @NonNull Collection<Tag> allTags(final @NonNull TagsUser user) {
        List<Tag> filtered = new ArrayList<>(this.idToTags.values());
        filtered.removeIf(tag -> !user.owns(this.permission, tag));

        return Collections.unmodifiableCollection(filtered);
    }

}
