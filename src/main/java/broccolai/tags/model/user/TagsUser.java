package broccolai.tags.model.user;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.impl.ConsoleTagsUser;
import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface TagsUser {

    TagsUser CONSOLE = new ConsoleTagsUser();

    @NonNull UUID uuid();

    void setCurrent(@Nullable Tag tag);

    boolean hasPermission(@NonNull Permission permission, @NonNull String perm);

    @NonNull Optional<Integer> current();

}
