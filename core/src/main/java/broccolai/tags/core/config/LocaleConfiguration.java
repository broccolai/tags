package broccolai.tags.core.config;

import broccolai.tags.core.model.locale.LocaleEntry;
import broccolai.tags.core.model.locale.impl.BasicLocaleEntry;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
public final class LocaleConfiguration implements Configuration {

    @Setting
    @Comment("This component is available in all other components with the replacement <prefix>")
    public LocaleEntry prefix = new BasicLocaleEntry("<gradient:#764ba2:#667eea><bold>TAGS »</bold></gradient>");

    @Setting
    @Comment("Locales for command feedback related messages")
    public FeedbackLocaleConfiguration commands = new FeedbackLocaleConfiguration();

    @Setting
    @Comment("Locales for menu related translations")
    public MenuLocaleConfiguration menu = new MenuLocaleConfiguration();

    @ConfigSerializable
    public static final class FeedbackLocaleConfiguration {

        public PlayerFeedbackLocaleConfiguration player = new PlayerFeedbackLocaleConfiguration();

        public AdminFeedbackLocaleConfiguration admin = new AdminFeedbackLocaleConfiguration();

        public ErrorLocaleConfiguration error = new ErrorLocaleConfiguration();

        @ConfigSerializable
        public static final class PlayerFeedbackLocaleConfiguration {

            @Setting
            public LocaleEntry select = new BasicLocaleEntry("<prefix> You have selected the tag <tag>");

            @Setting
            public LocaleEntry list = new BasicLocaleEntry("<prefix> You currently own these tags: ");

            @Setting
            public LocaleEntry listEntry = new BasicLocaleEntry("<gray>[</gray><name>: <tag><gray>]</gray> ");

            @Setting
            public LocaleEntry info = new BasicLocaleEntry("<prefix> Your <name> tag will appear like this: <tag> and is "
                    + "obtained through: <reason>");

        }

        @ConfigSerializable
        public static final class AdminFeedbackLocaleConfiguration {

            @Setting
            public LocaleEntry give = new BasicLocaleEntry("<prefix> Tag <tag> has been given to <target>");

            @Setting
            public LocaleEntry list = new BasicLocaleEntry("<prefix> These tags exist: ");

            @Setting
            public LocaleEntry listEntry = new BasicLocaleEntry("<gray>[</gray><name>: <tag><gray>]</gray> ");

            @Setting
            public LocaleEntry remove = new BasicLocaleEntry("<prefix> Tag <tag> has been removed from <target>");

            @Setting
            public LocaleEntry set = new BasicLocaleEntry("<prefix> <target> had their tag set to <tag>");

        }

        @ConfigSerializable
        public static final class ErrorLocaleConfiguration {

            @Setting
            public LocaleEntry userNotFound = new BasicLocaleEntry("<prefix> User not found for: <input>");

            @Setting
            public LocaleEntry tagNotFound = new BasicLocaleEntry("<prefix> Tag not found for: <input>");

        }

    }

    @ConfigSerializable
    public static final class MenuLocaleConfiguration {

        @Setting
        public LocaleEntry title = new BasicLocaleEntry("Tags Menu");

        @Setting
        public LocaleEntry equip = new BasicLocaleEntry("<gray>Left click to equip");

        @Setting
        public LocaleEntry currentlyEquipped = new BasicLocaleEntry("<green>Currently Equipped");

    }

    //region Configurate
    private static final @NonNull ObjectMapper<LocaleConfiguration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(LocaleConfiguration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static LocaleConfiguration loadFrom(final @NonNull ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @Override
    public <N extends ScopedConfigurationNode<N>> void saveTo(final @NonNull N node) throws SerializationException {
        MAPPER.save(this, node);
    }
    //endregion
}
