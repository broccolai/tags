package broccolai.tags.core.factory;

import broccolai.tags.core.commands.arguments.TagParser;
import broccolai.tags.core.commands.arguments.modes.TagParserMode;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CloudArgumentFactory {

    @NonNull TagParser tag(
            @Assisted("mode") @NonNull TagParserMode mode
    );

}
