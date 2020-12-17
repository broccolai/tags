package broccolai.tags.service.tags;

import broccolai.tags.model.tag.Tag;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Singleton
public final class TagsService {

    private static final MiniMessage MINI = MiniMessage.get();

    private final @NonNull Map<Integer, Tag> idToTags = new HashMap<>();
    private final @NonNull Map<String, Tag>  nameToTags = new HashMap<>();

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

    public Tag load(final int id) {
        return this.idToTags.get(id);
    }

    public Tag load(final String name) {
        return this.nameToTags.get(name);
    }

    public Collection<Tag> allTags() {
        return Collections.unmodifiableCollection(this.idToTags.values());
    }

}
