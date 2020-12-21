package broccolai.tags.model.user;

import broccolai.tags.model.tag.Tag;
import net.milkbowl.vault.permission.Permission;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Optional;
import java.util.UUID;

public interface TagsUser {

    @NonNull UUID uuid();

    void setCurrent(@Nullable Tag tag);

    boolean hasPermission(@NonNull Permission permissible, @NonNull String permission);

    @NonNull Optional<Integer> current();

}
