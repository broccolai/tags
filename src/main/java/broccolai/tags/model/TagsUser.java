package broccolai.tags.model;

import broccolai.tags.model.impl.ConsoleTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface TagsUser {

    TagsUser CONSOLE = new ConsoleTagsUser();

    @NonNull UUID getUuid();

}
