package broccolai.tags.core.config;

import org.checkerframework.checker.index.qual.Positive;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;
import org.spongepowered.configurate.objectmapping.meta.Setting;

@ConfigSerializable
@NonNull
public final class SqlConfiguration {

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
