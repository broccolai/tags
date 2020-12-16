package broccolai.tags.data.jdbi;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.service.tags.TagsService;
import com.google.inject.Inject;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TagsColumnMapper implements ColumnMapper<Tag> {

    private final @NonNull TagsService tags;

    @Inject
    public TagsColumnMapper(final @NonNull TagsService tags) {
        this.tags = tags;
    }

    @Override
    public Tag map(final @NonNull ResultSet r, final int columnNumber, final @NonNull StatementContext ctx) throws SQLException {
        int id = r.getInt(columnNumber);
        return this.tags.load(id);
    }

    @Override
    public Tag map(final @NonNull ResultSet r, final String columnLabel, final @NonNull StatementContext ctx) throws
            SQLException {
        int id = r.getInt(columnLabel);
        return this.tags.load(id);
    }

}
