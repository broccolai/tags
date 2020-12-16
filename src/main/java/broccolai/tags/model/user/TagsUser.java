package broccolai.tags.model.user;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.impl.PlayerTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public interface TagsUser {

    @NonNull UUID uuid();

    void setCurrent(@Nullable Tag tag);

    @NonNull Optional<Tag> current();

    @NonNull Collection<Tag> tags();

    class Builder {

        private final UUID uniqueId;
        private final Integer currentTag;
        private final Map<Integer, Tag> tags = new HashMap<>();

        public Builder(final UUID uniqueId, final Integer currentTag) {
            this.uniqueId = uniqueId;
            this.currentTag = currentTag;
        }

        public @NonNull Builder tag(final Tag tag) {
            this.tags.put(tag.id(), tag);

            return this;
        }

        public @NonNull TagsUser build() {
            return new PlayerTagsUser(uniqueId, tags, currentTag);
        }

    }

}
