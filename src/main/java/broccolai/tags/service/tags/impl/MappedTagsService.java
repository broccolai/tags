package broccolai.tags.service.tags.impl;

import broccolai.tags.config.Configuration;
import broccolai.tags.config.TagConfiguration;
import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import com.google.inject.Inject;
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

public final class MappedTagsService implements TagsService {

    private static final MiniMessage MINI = MiniMessage.get();

    private final @NonNull Permission permission;

    private final @NonNull Map<Integer, Tag> idToTags = new HashMap<>();
    private final @NonNull Map<String, Tag> nameToTags = new HashMap<>();

    @Inject
    public MappedTagsService(final @NonNull Configuration configuration, final @NonNull Permission permission) {
        this.permission = permission;

        for (TagConfiguration config : configuration.tags) {
            this.create(config.id, config.name, config.secret, config.component, config.reason);
        }
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
    public Tag load(final @NonNull String name) {
        return this.nameToTags.get(name);
    }

    @Override
    public void grant(@NonNull final TagsUser user, @NonNull final Tag tag) {
        user.addPermission(this.permission, "tags.tag." + tag.id());
    }

    @Override
    public @NonNull Collection<Tag> allTags() {
        return Collections.unmodifiableCollection(this.idToTags.values());
    }

    @Override
    public @NonNull Collection<Tag> allTags(final @NonNull TagsUser user) {
        List<Tag> filtered = new ArrayList<>(this.idToTags.values());
        filtered.removeIf(tag -> !user.hasPermission(permission, "tags.tag." + tag.id()));

        return Collections.unmodifiableCollection(filtered);
    }

}
