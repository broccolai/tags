package broccolai.tags.model.user;

import broccolai.tags.model.user.impl.ConsoleTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

public interface TagsUser {

    TagsUser CONSOLE = new ConsoleTagsUser();

    @NonNull UUID getUuid();

}
