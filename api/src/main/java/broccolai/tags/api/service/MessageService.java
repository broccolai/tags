package broccolai.tags.api.service;

import broccolai.tags.api.model.Service;
import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import java.util.Collection;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface MessageService extends Service {

    TagResolver prefix();

    Component commandSelect(@NonNull ConstructedTag tag);

    Component commandList(@NonNull Collection<ConstructedTag> tags);

    Component commandInfo(@NonNull ConstructedTag tag);

    Component commandAdminGive(@NonNull ConstructedTag tag, @NonNull TagsUser target);

    Component commandAdminRemove(@NonNull ConstructedTag tag, @NonNull TagsUser target);

    Component commandAdminList(@NonNull Collection<ConstructedTag> tags);

    Component commandAdminSet(@NonNull ConstructedTag tag, @NonNull TagsUser target);

    Component commandErrorUserNotFound(@NonNull String input);

    Component commandErrorTagNotFound(@NonNull String input);

}
