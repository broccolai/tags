package broccolai.tags.api.model.user;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.impl.ConsoleTagsUser;
import java.util.Optional;
import java.util.UUID;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public interface TagsUser {

    TagsUser CONSOLE = new ConsoleTagsUser();

    @NonNull UUID uuid();

    void current(@Nullable ConstructedTag tag);

    @NonNull Optional<Integer> current();

}
