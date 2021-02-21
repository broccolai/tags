setupShadowJar()

dependencies {
    api(project(":tags-core"))

    compileOnly("com.destroystokyo.paper", "paper-api", Versions.PAPER)
    compileOnly("me.clip", "placeholderapi", Versions.PAPI)
    compileOnly("com.github.MilkBowl", "VaultAPI", Versions.VAULT)

    api("net.kyori", "adventure-platform-bukkit", Versions.ADVENTURE_PLATFORM)

    api("cloud.commandframework", "cloud-paper", Versions.CLOUD)
    api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)
}
