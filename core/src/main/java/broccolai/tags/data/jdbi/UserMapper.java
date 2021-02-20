package broccolai.tags.data.jdbi;

import broccolai.tags.model.user.TagsUser;
import broccolai.tags.model.user.impl.PlayerTagsUser;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public final class UserMapper implements RowMapper<TagsUser> {

    @Override
    public @NonNull TagsUser map(final @NonNull ResultSet rs, final @NonNull StatementContext ctx) throws SQLException {
        ColumnMapper<UUID> uuidMapper = ctx.findColumnMapperFor(UUID.class).orElseThrow(IllegalStateException::new);

        UUID uniqueId = uuidMapper.map(rs, "uuid", ctx);
        Integer currentTag = rs.getInt("currentTag");

        return new PlayerTagsUser(uniqueId, currentTag);
    }

}
