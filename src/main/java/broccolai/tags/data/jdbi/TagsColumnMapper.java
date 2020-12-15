package broccolai.tags.data.jdbi;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.service.tags.TagsService;
import com.google.inject.Inject;
import org.jdbi.v3.core.mapper.ColumnMapper;
import org.jdbi.v3.core.statement.StatementContext;

import java.sql.ResultSet;
import java.sql.SQLException;

public final class TagsColumnMapper implements ColumnMapper<Tag> {

    private final TagsService tags;

    @Inject
    public TagsColumnMapper(final TagsService tags) {
        this.tags = tags;
    }

    @Override
    public Tag map(final ResultSet r, final int columnNumber, final StatementContext ctx) throws SQLException {
        int id = r.getInt(columnNumber);
        return this.tags.load(id);
    }

    @Override
    public Tag map(final ResultSet r, final String columnLabel, final StatementContext ctx) throws SQLException {
        int id = r.getInt(columnLabel);
        return this.tags.load(id);
    }

}
