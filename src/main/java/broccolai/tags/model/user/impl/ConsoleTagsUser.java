package broccolai.tags.model.user.impl;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import broccolai.tags.service.tags.TagsService;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

public final class ConsoleTagsUser implements TagsUser {

    public static final @NonNull UUID UUID = new UUID(0, 0);
    private final @NonNull TagsService tagsService;

    private Integer currentTag;

    public ConsoleTagsUser(final @NonNull TagsService tagsService) {
        this.tagsService = tagsService;
    }

    @Override
    public @NonNull UUID uuid() {
        return UUID;
    }

    @Override
    public void setCurrent(final @Nullable Tag tag) {
        this.currentTag = tag != null ? tag.id() : null;
    }

    @Override
    public @NonNull Optional<Tag> current() {
        return Optional.ofNullable(this.tagsService.load(this.currentTag));
    }

    @Override
    public @NonNull Collection<Tag> tags() {
        return this.tagsService.allTags();
    }

}
