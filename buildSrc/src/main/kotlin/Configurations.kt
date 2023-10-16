import com.github.jengelman.gradle.plugins.shadow.ShadowPlugin
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.invoke
import org.gradle.kotlin.dsl.withType

fun Project.setupShadowJar() {
    apply<ShadowPlugin>()

    tasks {
        withType<ShadowJar> {

            dependencies {
                exclude(dependency("com.google.guava:"))
                exclude(dependency("com.google.errorprone:"))
                exclude(dependency("org.checkerframework:"))
            }

            relocate(
                    rootProject.group,
                    "com.github.benmanes.caffeine",
                    "com.typesafe.config",
                    "com.zaxxer.hikari",
                    "com.google.inject",
                    "org.antlr",
                    "org.jdbi",
                    "org.aopalliance",
                    "org.spongepowered.configurate",
                    "io.leangen.geantyref",
                    "cloud.commandframework",
                    "net.kyori.event",
                    "net.kyori.coffee",
                    "org.objectweb.asm",
                    "org.flyway",
                    "broccolai.corn"
            )

            archiveFileName.set(project.name + ".jar")
        }

        getByName("build") {
            dependsOn(withType<ShadowJar>())
        }
    }
}

private fun ShadowJar.relocate(group: Any, vararg dependencies: String) {
    dependencies.forEach {
        val split = it.split('.')
        val name = split.last()
        relocate(it, "$group.dependencies.$name")
    }
}
