package broccolai.tags.config;

import com.google.inject.Singleton;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
@Singleton
@NonNull
public final class Configuration {

    @Setting
    public StorageConfiguration storage = new StorageConfiguration();

    @Setting
    @Comment(
            "Potential tags for players to obtain. \n"
                    + "Increment id for each new tag, if you remove a tag, treat the config as if it's id is still there. \n"
                    + "Permissions will use this id as it's reference. \n"
                    + "The name attribute should be a simple one word phrase for selecting tags through commands."
    )
    public List<TagConfiguration> tags = new ArrayList<TagConfiguration>() {{
        this.add(new TagConfiguration(1, "example", false, "<red><bold>example", "Acquired by playing for an hour"));
    }};

    //region Configurate
    private static final @NonNull ObjectMapper<Configuration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(Configuration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Configuration loadFrom(final @NonNull ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    public <N extends ScopedConfigurationNode<N>> void saveTo(final @NonNull N node) throws SerializationException {
        MAPPER.save(this, node);
    }
    //endregion
}
