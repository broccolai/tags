import net.kyori.indra.IndraPublishingPlugin
import net.kyori.indra.IndraCheckstylePlugin
import net.kyori.indra.IndraPlugin
import net.kyori.indra.sonatypeSnapshots

plugins {
    id("net.kyori.indra") version Versions.INDRA
    id("net.kyori.indra.publishing") version Versions.INDRA
    id("net.kyori.indra.checkstyle") version Versions.INDRA
}

group = "broccolai.tags"
version = "1.1.3"

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
        maven("https://repo.broccol.ai")
    }

    tasks {
        indra {
            gpl3OnlyLicense()
            publishReleasesTo("broccolai", "https://repo.broccol.ai/releases")

            javaVersions {
                target.set(8)
            }

            github("broccolai", "tickets") {
                ci = true
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
