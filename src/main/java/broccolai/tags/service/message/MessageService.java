package broccolai.tags.service.message;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.Template;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

public interface MessageService {

    Template prefix();

    Component commandSelect(@NonNull Tag tag);

    Component commandList(@NonNull Collection<Tag> tags);

    Component commandPreview(@NonNull Tag tag);

    Component commandAdminGive(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandAdminRemove(@NonNull Tag tag, @NonNull TagsUser target);

    Component commandAdminList(@NonNull Collection<Tag> tags);
}
