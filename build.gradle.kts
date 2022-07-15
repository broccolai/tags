import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.repository.sonatypeSnapshots

plugins {
    id("net.kyori.indra")
    id("net.kyori.indra.publishing")
    id("net.kyori.indra.checkstyle")
    id("com.github.johnrengelman.shadow")
    id("com.github.ben-manes.versions")
    id("net.ltgt.errorprone")
}

group = "broccolai.tags"
version = "1.3.0-SNAPSHOT"

subprojects {
    apply<IndraPlugin>()
    apply<IndraPublishingPlugin>()
    apply<IndraCheckstylePlugin>()

    repositories {
        mavenCentral()
        sonatypeSnapshots()
        maven("https://papermc.io/repo/repository/maven-public/")
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
        maven("https://jitpack.io")
        maven("https://repo.broccol.ai/releases")
    }

    tasks {
        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target(17)
            }

            github("broccolai", "tags") {
                ci(true)
                publishing(true)
            }
        }

        processResources {
            expand("version" to rootProject.version)
        }
    }
}

tasks {
    withType<Jar> {
        onlyIf { false }
    }
}
