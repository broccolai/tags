package broccolai.tags.factory;

import broccolai.tags.commands.arguments.TagArgument;
import broccolai.tags.commands.arguments.UserArgument;
import com.google.inject.assistedinject.Assisted;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CloudArgumentFactory {

    @NonNull TagArgument tag(@Assisted("name") @NonNull String name, @Assisted("shouldCheck") boolean shouldCheck);

    @NonNull UserArgument user(@Assisted("name") @NonNull String name);

}
