package broccolai.tags.core.service.message;

import broccolai.tags.api.model.tag.ConstructedTag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.config.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.Tag;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.Collection;

@Singleton
public final class MiniMessageService implements MessageService {

    private final LocaleConfiguration locale;
    private final UserService userService;

    @Inject
    public MiniMessageService(
            final @NonNull LocaleConfiguration locale,
            final @NonNull UserService userService
    ) {
        this.locale = locale;
        this.userService = userService;
    }

    @Override
    public TagResolver prefix() {
        return TagResolver.resolver("prefix", Tag.inserting(this.locale.prefix.asComponent()));
    }

    @Override
    public Component commandSelect(final @NonNull ConstructedTag tag) {
        TagResolver tagComponent = TagResolver.resolver("tag", Tag.inserting(tag.component()));

        return this.locale.commands.player.select.asComponent(this.prefix(), tagComponent);
    }

    @Override
    public Component commandList(final @NonNull Collection<ConstructedTag> tags) {
        Component component = this.locale.commands.player.list.asComponent(this.prefix());

        for (ConstructedTag tag : tags) {
            TagResolver nameTemplate = TagResolver.resolver("name", Tag.preProcessParsed(tag.name()));
            TagResolver tagComponent = TagResolver.resolver("tag", Tag.inserting(tag.component()));

            component = component.append(this.locale.commands.player.listEntry.asComponent(nameTemplate, tagComponent));
        }

        return component;
    }

    @Override
    public Component commandInfo(final @NonNull ConstructedTag tag) {
        TagResolver nameTemplate = TagResolver.resolver("name", Tag.preProcessParsed(tag.name()));
        TagResolver tagTemplate = TagResolver.resolver("tag", Tag.inserting(tag.component()));
        TagResolver reasonTemplate = TagResolver.resolver("reason", Tag.preProcessParsed(tag.reason()));

        return this.locale.commands.player.info.asComponent(this.prefix(), nameTemplate, tagTemplate, reasonTemplate);
    }

    @Override
    public Component commandAdminGive(
            final @NonNull ConstructedTag tag,
            final @NonNull TagsUser target
    ) {
        TagResolver tagComponent = TagResolver.resolver("tag", Tag.inserting(tag.component()));
        TagResolver targetComponent = TagResolver.resolver("target", Tag.preProcessParsed(this.nameFromUser(target)));

        return this.locale.commands.admin.give.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminRemove(
            final @NonNull ConstructedTag tag,
            final @NonNull TagsUser target
    ) {
        TagResolver tagComponent = TagResolver.resolver("tag", Tag.inserting(tag.component()));
        TagResolver targetComponent = TagResolver.resolver("target", Tag.preProcessParsed(this.nameFromUser(target)));

        return this.locale.commands.admin.remove.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminList(final @NonNull Collection<ConstructedTag> tags) {
        Component component = this.locale.commands.admin.list.asComponent(this.prefix());

        for (ConstructedTag tag : tags) {
            TagResolver nameTemplate = TagResolver.resolver("name", Tag.preProcessParsed(tag.name()));
            TagResolver tagTemplate = TagResolver.resolver("tag", Tag.inserting(tag.component()));

            component = component.append(this.locale.commands.admin.listEntry.asComponent(nameTemplate, tagTemplate));
        }

        return component;
    }

    @Override
    public Component commandAdminSet(@NonNull final ConstructedTag tag, @NonNull final TagsUser target) {
        TagResolver tagTemplate = TagResolver.resolver("tag", Tag.inserting(tag.component()));
        TagResolver targetTemplate = TagResolver.resolver("target", Tag.preProcessParsed(this.nameFromUser(target)));

        return this.locale.commands.admin.set.asComponent(this.prefix(), tagTemplate, targetTemplate);
    }

    @Override
    public Component commandErrorUserNotFound(final @NonNull String input) {
        TagResolver inputTemplate = TagResolver.resolver("input", Tag.preProcessParsed(input));

        return this.locale.commands.error.userNotFound.asComponent(this.prefix(), inputTemplate);
    }

    @Override
    public Component commandErrorTagNotFound(final @NonNull String input) {
        TagResolver inputTemplate = TagResolver.resolver("input", Tag.preProcessParsed(input));

        return this.locale.commands.error.tagNotFound.asComponent(this.prefix(), inputTemplate);
    }

    //todo: Add user to TagUser object?
    private String nameFromUser(final @NonNull TagsUser user) {
        return this.userService.name(user);
    }

}
