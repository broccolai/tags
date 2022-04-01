package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface MessageService extends Service {

    TagResolver prefix();

    Component commandSelect(@NonNull Tag tag);

    Component commandList(@NonNull Collection<Tag> tags);

    Component commandInfo(@NonNull Tag tag);

    Component commandAdminGive(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandAdminRemove(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandAdminList(@NonNull Collection<Tag> tags);

    Component commandAdminSet(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandErrorUserNotFound(@NonNull String input);

    Component commandErrorTagNotFound(@NonNull String input);

}
