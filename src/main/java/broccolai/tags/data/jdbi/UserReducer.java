package broccolai.tags.data.jdbi;

import broccolai.tags.model.tag.Tag;
import broccolai.tags.model.user.TagsUser.Builder;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jdbi.v3.core.result.RowReducer;
import org.jdbi.v3.core.result.RowView;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Stream;

public final class UserReducer implements RowReducer<Map<UUID, Builder>, Builder> {

    @Override
    public @NonNull Map<@NonNull UUID, @NonNull Builder> container() {
        return new HashMap<>();
    }

    @Override
    public void accumulate(final @NonNull Map<UUID, Builder> container, final RowView rowView) {
        UUID id = rowView.getColumn("uuid", UUID.class);

        Builder builder = container.computeIfAbsent(id, $ -> rowView.getRow(Builder.class));

        if (rowView.getColumn("owner", UUID.class) != null) {
            builder = builder.tag(rowView.getColumn("id", Tag.class));
            container.put(id, builder);
        }
    }

    @Override
    public @NonNull Stream<Builder> stream(final @NonNull Map<UUID, Builder> container) {
        return container.values().stream();
    }

}
