package broccolai.tags.config;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.ConfigurationNode;
import org.spongepowered.configurate.ScopedConfigurationNode;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.ObjectMapper;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;
import org.spongepowered.configurate.serialize.SerializationException;

@ConfigSerializable
@NonNull
public final class Configuration {

    private static final ObjectMapper<Configuration> MAPPER;

    static {
        try {
            MAPPER = ObjectMapper.factory().get(Configuration.class);
        } catch (final SerializationException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static Configuration loadFrom(final ConfigurationNode node) throws SerializationException {
        return MAPPER.load(node);
    }

    @Setting
    public SqlConfig sql = new SqlConfig();

    public <N extends ScopedConfigurationNode<N>> void saveTo(final N node) throws SerializationException {
        MAPPER.save(this, node);
    }

    @ConfigSerializable
    @NonNull
    public static final class SqlConfig {

        @Setting()
        @Comment("SQL JDBC URI")
        public String jdbcUri = "jdbc:mysql://localhost:3306/tags";

        @Setting()
        @Comment("SQL Server username")
        public String username = "username";

        @Setting()
        @Comment("SQL Server password")
        public String password = "****";

        @Setting()
        @Comment("Pool connection limit")
        public @Positive int maxConnections = 10;

    }

}
