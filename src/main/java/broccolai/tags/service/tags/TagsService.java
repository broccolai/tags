package broccolai.tags.service.tags;

import broccolai.tags.model.tag.Tag;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;

import java.util.HashMap;
import java.util.Map;

@Singleton
public final class TagsService {
    private static final MiniMessage MINI = MiniMessage.get();

    private final Map<Integer, Tag> tags = new HashMap<>();

    public void create(final int id, final boolean secret, final String componentString, final String reason) {
        Component component = MINI.parse(componentString);

        Tag tag = new Tag(id, secret, component, reason);

        this.tags.put(id, tag);
    }

    public Tag load(final int id) {
        return this.tags.get(id);
    }
}
