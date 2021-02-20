package broccolai.tags.bukkit;

import broccolai.tags.bukkit.inject.PlatformModule;
import broccolai.tags.bukkit.inject.VaultModule;
import broccolai.tags.core.TagsPlugin;
import broccolai.tags.bukkit.integrations.PapiIntegration;
import broccolai.tags.bukkit.integrations.VaultIntegration;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class TagsPlatform extends JavaPlugin {

    @Override
    public void onEnable() {
        Injector injector = Guice.createInjector(
                new PlatformModule(this),
                new VaultModule()
        );

        TagsPlugin plugin = new TagsPlugin();
        injector = plugin.preload(injector);

        this.getServer().getPluginManager().registerEvents(
                injector.getInstance(VaultIntegration.class),
                this
        );

        if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            injector.getInstance(PapiIntegration.class).register();
        }
    }

}
