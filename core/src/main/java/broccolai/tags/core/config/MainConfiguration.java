package broccolai.tags.core.config;

import broccolai.tags.api.model.tag.TagDisplayInformation;
import com.google.inject.Singleton;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@Singleton
@NonNull
public final class MainConfiguration implements Configuration {

    @Setting
    public StorageConfiguration storage = new StorageConfiguration();

    @Setting
    @Comment("Default tag to use when player has none selected")
    public int defaultTag = 1;

    @Setting
    @Comment(
        """
            Potential tags for players to obtain.
            Increment id for each new tag, if you remove a tag, treat the config as if it's id is still there.
            Permissions will use this id as it's reference.
            The name attribute should be a simple one word phrase for selecting tags through commands.
                                
            Tag configuration is described as followed:
                id -> the unique numerical id for tags
                name -> the unique name for a tag to be used in a command
                component -> the component to be rendered in chat / menu first line
                reason -> how a player could obtain the tag
                display-information:
                    material -> the material id to use for the menu representation
                    custom-model-data -> the custom model id to use on items, optionally
            """
    )
    public List<TagConfiguration> tags = List.of(
        new TagConfiguration(
            1,
            "example",
            false,
            "<red><bold>example</bold></red>",
            "Acquired by playing for an hour",
            new TagDisplayInformation(
                "stick",
                5
            )
        )
    );

    //region Configurate
    private static final @NonNull ObjectMapper<MainConfiguration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(MainConfiguration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static MainConfiguration loadFrom(final @NonNull ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @Override
    public <N extends ScopedConfigurationNode<N>> void saveTo(final @NonNull N node) throws SerializationException {
        MAPPER.save(this, node);
    }
    //endregion
}
