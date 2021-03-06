setupShadowJar()

dependencies {
    api(project(":tags-core"))

    compileOnly("org.spigotmc", "spigot-api", Versions.SPIGOT)
    compileOnly("me.clip", "placeholderapi", Versions.PAPI)
    compileOnly("com.github.MilkBowl", "VaultAPI", Versions.VAULT)

    api("net.kyori", "adventure-platform-bukkit", Versions.ADVENTURE_PLATFORM)

    api("cloud.commandframework", "cloud-paper", Versions.CLOUD)
    api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)
}
