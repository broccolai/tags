package broccolai.tags.model.user;

import broccolai.tags.model.user.impl.ConsoleTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface TagsUser {

    @NonNull UUID uuid();

    void setCurrent(@Nullable Tag tag);

    @NonNull Optional<Tag> current();

    @NonNull Collection<Tag> tags();

    @NonNull UUID getUuid();

}
