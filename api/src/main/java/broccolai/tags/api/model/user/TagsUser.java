package broccolai.tags.api.model.user;

import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.impl.ConsoleTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface TagsUser {

    TagsUser CONSOLE = new ConsoleTagsUser();

    @NonNull UUID uuid();

    void setCurrent(@Nullable Tag tag);

    @NonNull Optional<Integer> current();

}
