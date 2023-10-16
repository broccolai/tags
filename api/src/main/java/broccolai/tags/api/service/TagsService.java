package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.tag.TagDisplayInformation;
import broccolai.tags.api.model.user.TagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

public interface TagsService extends Service {

    @NotNull ConstructedTag defaultTag();

    void create(
            int id,
            @NonNull String name,
            boolean secret,
            @NonNull String componentString,
            @NonNull String reason,
            @Nullable TagDisplayInformation information
    );

    @Nullable ConstructedTag load(int id);

    @Nullable ConstructedTag load(@NonNull String name);

    @NonNull ConstructedTag load(@NonNull TagsUser user);

    @NonNull Collection<@NonNull ConstructedTag> allTags();

    @NonNull Collection<@NonNull ConstructedTag> allTags(@NonNull TagsUser user);

}
