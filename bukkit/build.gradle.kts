plugins {
    id("xyz.jpenilla.run-paper") version "1.0.6"
}

setupShadowJar()

dependencies {
    api(project(":tags-core"))

    compileOnly("io.papermc.paper", "paper-api", Versions.PAPER)
    compileOnly("me.clip", "placeholderapi", Versions.PAPI)
    compileOnly("com.github.MilkBowl", "VaultAPI", Versions.VAULT)

    api("cloud.commandframework", "cloud-paper", Versions.CLOUD)
    api("cloud.commandframework", "cloud-minecraft-extras", Versions.CLOUD)
}

tasks {
    runServer {
        minecraftVersion("1.18.2")
    }
}
