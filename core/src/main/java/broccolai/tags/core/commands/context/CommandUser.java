package broccolai.tags.core.commands.context;

import java.util.UUID;
import net.kyori.adventure.audience.ForwardingAudience;
import org.checkerframework.checker.nullness.qual.NonNull;

public interface CommandUser extends ForwardingAudience.Single {

    UUID uuid();

    @SuppressWarnings("unchecked")
    default <A extends CommandUser> @NonNull A cast() {
        return (A) this;
    }

}
