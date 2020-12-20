package broccolai.tags.config;

import com.google.inject.Singleton;
import org.checkerframework.checker.index.qual.Positive;
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

    @Setting
    public SqlConfig sql = new SqlConfig();

    @Setting
    @Comment(
            "Potential tags for players to obtain. \n"
                    + "Increment id for each new tag, if you remove a tag, treat the config as if it's id is still there. \n"
                    + "Permissions will use this id as it's reference. \n"
                    + "The name attribute should be a simple one word phrase for selecting tags through commands."
    )
    public List<TagConfig> tags = new ArrayList<TagConfig>() {{
        this.add(new TagConfig(1, "example", "<red><bold>example"));
    }};

    public <N extends ScopedConfigurationNode<N>> void saveTo(final @NonNull N node) throws SerializationException {
        MAPPER.save(this, node);
    }

    @ConfigSerializable
    @NonNull
    public static final class SqlConfig {

        @Setting
        @Comment("SQL JDBC URI")
        public String jdbcUri = "jdbc:mysql://localhost:3306/tags";

        @Setting
        @Comment("SQL Server username")
        public String username = "username";

        @Setting
        @Comment("SQL Server password")
        public String password = "****";

        @Setting
        @Comment("Pool connection limit")
        public @Positive int maxConnections = 10;

    }

    @ConfigSerializable
    @NonNull
    public static final class TagConfig {

        @Setting
        @Comment("Tags unique id")
        public int id;

        @Setting
        @Comment("Readable name for players")
        public String name;

        @Setting
        @Comment("MiniMessage component to display")
        public String component;

        public TagConfig() {
        }

        public TagConfig(final int id, final String name, final String component) {
            this.id = id;
            this.name = name;
            this.component = component;
        }

    }

}
