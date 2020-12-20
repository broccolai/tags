package broccolai.tags.inject;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.checkerframework.checker.nullness.qual.NonNull;

public final class VaultModule extends AbstractModule {

    @Provides
    @Singleton
    private Permission providePermission(final @NonNull Plugin plugin) {
        RegisteredServiceProvider<Permission> rsp = plugin.getServer().getServicesManager().getRegistration(Permission.class);

        if (rsp == null) {
            throw new RuntimeException();
        }

        return rsp.getProvider();
    }

}
