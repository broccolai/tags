package broccolai.tags.service.tags;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

public interface TagsService {

    @NotNull Tag defaultTag();

    void create(
            int id,
            @NonNull String name,
            boolean secret,
            @NonNull String componentString,
            @NonNull String reason
    );

    @Nullable Tag load(int id);

    @Nullable Tag load(@NonNull String name);

    @NonNull Tag load(@NonNull TagsUser user);

    @NonNull Collection<@NonNull Tag> allTags();

    @NonNull Collection<@NonNull Tag> allTags(@NonNull TagsUser user);

}
