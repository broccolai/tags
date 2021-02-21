package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TagsService extends Service {

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
