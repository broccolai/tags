package broccolai.tags.core.service.message;

import broccolai.tags.api.model.tag.Tag;
import broccolai.tags.api.model.user.TagsUser;
import broccolai.tags.api.service.MessageService;
import broccolai.tags.api.service.UserService;
import broccolai.tags.core.config.LocaleConfiguration;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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
        return Placeholder.component("prefix", this.locale.prefix.asComponent());
    }

    @Override
    public Component commandSelect(final @NonNull Tag tag) {
        TagResolver tagComponent = Placeholder.component("tag", tag.component());

        return this.locale.commands.player.select.asComponent(this.prefix(), tagComponent);
    }

    @Override
    public Component commandList(final @NonNull Collection<Tag> tags) {
        Component component = this.locale.commands.player.list.asComponent(this.prefix());

        for (Tag tag : tags) {
            TagResolver nameTagResolver = Placeholder.unparsed("name", tag.name());
            TagResolver tagComponent = Placeholder.component("tag", tag.component());

            component = component.append(this.locale.commands.player.listEntry.asComponent(nameTagResolver, tagComponent));
        }

        return component;
    }

    @Override
    public Component commandInfo(final @NonNull Tag tag) {
        TagResolver nameTagResolver = Placeholder.unparsed("name", tag.name());
        TagResolver tagTagResolver = Placeholder.component("tag", tag.component());
        TagResolver reasonTagResolver = Placeholder.parsed("reason", tag.reason());

        return this.locale.commands.player.info.asComponent(this.prefix(), nameTagResolver, tagTagResolver, reasonTagResolver);
    }

    @Override
    public Component commandAdminGive(
            final @NonNull Tag tag,
            final @NonNull TagsUser target
    ) {
        TagResolver tagComponent = Placeholder.component("tag", tag.component());
        TagResolver targetComponent = Placeholder.unparsed("target", this.nameFromUser(target));

        return this.locale.commands.admin.give.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminRemove(
            final @NonNull Tag tag,
            final @NonNull TagsUser target
    ) {
        TagResolver tagComponent = Placeholder.component("tag", tag.component());
        TagResolver targetComponent = Placeholder.unparsed("target", this.nameFromUser(target));

        return this.locale.commands.admin.remove.asComponent(this.prefix(), tagComponent, targetComponent);
    }

    @Override
    public Component commandAdminList(final @NonNull Collection<Tag> tags) {
        Component component = this.locale.commands.admin.list.asComponent(this.prefix());

        for (Tag tag : tags) {
            TagResolver nameTagResolver = Placeholder.unparsed("name", tag.name());
            TagResolver tagTagResolver = Placeholder.component("tag", tag.component());

            component = component.append(this.locale.commands.admin.listEntry.asComponent(nameTagResolver, tagTagResolver));
        }

        return component;
    }

    @Override
    public Component commandAdminSet(@NonNull final Tag tag, @NonNull final TagsUser target) {
        TagResolver tagTagResolver = Placeholder.component("tag", tag.component());
        TagResolver targetTagResolver = Placeholder.unparsed("target", this.nameFromUser(target));

        return this.locale.commands.admin.set.asComponent(this.prefix(), tagTagResolver, targetTagResolver);
    }

    @Override
    public Component commandErrorUserNotFound(final @NonNull String input) {
        TagResolver inputTagResolver = Placeholder.parsed("input", input);

        return this.locale.commands.error.userNotFound.asComponent(this.prefix(), inputTagResolver);
    }

    @Override
    public Component commandErrorTagNotFound(final @NonNull String input) {
        TagResolver inputTagResolver = Placeholder.parsed("input", input);

        return this.locale.commands.error.tagNotFound.asComponent(this.prefix(), inputTagResolver);
    }

    //todo: Add user to TagUser object?
    private String nameFromUser(final @NonNull TagsUser user) {
        return this.userService.name(user);
    }

}
