package broccolai.tags.core.service.tags;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.tag.TagDisplayInformation;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.PermissionService;
import broccolai.tags.api.service.TagsService;
import broccolai.tags.core.config.MainConfiguration;
import broccolai.tags.core.config.TagConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

@Singleton
public final class MappedTagsService implements TagsService {

    private static final MiniMessage MINI = MiniMessage.miniMessage();

    private final @NonNull Map<Integer, ConstructedTag> idToTags = new HashMap<>();
    private final @NonNull Map<String, ConstructedTag> nameToTags = new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

    private final @NonNull PermissionService permissionService;
    private final int defaultId;

    @Inject
    public MappedTagsService(
            final @NonNull MainConfiguration mainConfiguration,
            final @NonNull PermissionService permissionService
    ) {
        this.permissionService = permissionService;
        this.defaultId = mainConfiguration.defaultTag;

        for (TagConfiguration config : mainConfiguration.tags) {
            this.create(config.id, config.name, config.secret, config.component, config.reason, config.displayInformation);
        }
    }

    @Override
    public @NotNull ConstructedTag defaultTag() {
        return this.load(this.defaultId);
    }

    @Override
    public void create(
            final int id,
            final @NonNull String name,
            final boolean secret,
            final @NonNull String componentString,
            final @NonNull String reason,
            final @Nullable TagDisplayInformation displayInformation
    ) {
        Component component = MINI.deserialize(componentString);

        ConstructedTag tag = new ConstructedTag(id, name, secret, component, reason, displayInformation);

        this.idToTags.put(id, tag);
        this.nameToTags.put(name.toLowerCase(), tag);
    }

    @Override
    public @Nullable ConstructedTag load(final int id) {
        return this.idToTags.get(id);
    }

    @Override
    public @Nullable ConstructedTag load(final @NonNull String name) {
        return this.nameToTags.get(name);
    }

    @Override
    public @NonNull ConstructedTag load(final @NonNull TagsUser user) {
        Optional<ConstructedTag> current = user.current()
                .map(this::load)
                .filter(tag -> this.permissionService.has(user, tag));

        if (current.isPresent()) {
            return current.get();
        }

        List<Integer> keys = new ArrayList<>(this.idToTags.keySet());
        Collections.sort(keys);
        Collections.reverse(keys);

        for (int index : keys) {
            ConstructedTag tag = this.idToTags.get(index);
            if (this.permissionService.has(user, tag)) {
                return tag;
            }
        }

        return this.defaultTag();
    }

    @Override
    public @NonNull Collection<ConstructedTag> allTags() {
        return Collections.unmodifiableCollection(this.idToTags.values());
    }

    @Override
    public @NonNull Collection<ConstructedTag> allTags(final @NonNull TagsUser user) {
        List<ConstructedTag> filtered = new ArrayList<>(this.idToTags.values());
        filtered.removeIf(tag -> !this.permissionService.has(user, tag));

        return Collections.unmodifiableCollection(filtered);
    }

}
