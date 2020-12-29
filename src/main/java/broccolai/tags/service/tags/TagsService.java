package broccolai.tags.service.tags;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;

@Singleton
public interface TagsService {

    void create(
            int id,
            @NonNull String name,
            boolean secret,
            @NonNull String componentString,
            @NonNull String reason
    );

    @Nullable Tag load(int id);

    @Nullable Tag load(@NonNull String name);

    @NonNull Collection<@NonNull Tag> allTags();

    @NonNull Collection<@NonNull Tag> allTags(@NonNull TagsUser user);

}
