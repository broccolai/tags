package broccolai.tags.core.factory;

import broccolai.tags.core.commands.arguments.TagArgument;
import broccolai.tags.core.commands.arguments.UserArgument;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CloudArgumentFactory {

    @NonNull TagArgument tag(
            @Assisted("name") @NonNull String name,
            @Assisted("mode") @NonNull TagParserMode mode
    );

    @NonNull UserArgument user(
            @Assisted("name") @NonNull String name,
            @Assisted("required") boolean required
    );

}
