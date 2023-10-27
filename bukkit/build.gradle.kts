import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    id("xyz.jpenilla.run-paper") version "2.2.0"
    id("com.github.johnrengelman.shadow")
}

dependencies {
    api(project(":tags-core"))

    compileOnly(libs.paper.api)
    compileOnly(libs.papi)
    compileOnly(libs.vault)

    api(libs.cloud.paper)
    api(libs.cloud.extras)
    api(libs.corn.minecraft.paper)

    api(libs.interfaces.paper)

    implementation(libs.h2)
}

tasks {
    runServer {
        minecraftVersion("1.20.2")
        downloadPlugins {
            url("https://github.com/MilkBowl/Vault/releases/download/1.7.3/Vault.jar")
        }
    }

    fun Project.collectDependencies(): Set<Dependency> {
        val api = configurations.api.get()
        val implementation = configurations.implementation.get()
        return (api.dependencies + implementation.dependencies).toSet()
    }

    fun Set<Dependency>.formatDependencies(): List<String> = flatMap { dependency ->
        when (dependency) {
            is ProjectDependency -> {
                dependency.dependencyProject.collectDependencies().formatDependencies()
            }

            else -> {
                val formatted = dependency.run { "$group:$name:$version" }
                listOf(formatted)
            }
        }
    }

    register("writeDependenciesToFile") {
        val outputDir = File("${buildDir}/resources/main")
        outputDir.mkdirs()
        val outputFile = File(outputDir, "libraries.txt")

        val dependencies = project
            .collectDependencies()
            .formatDependencies()

        outputFile.writeText(dependencies.joinToString("\n"))
    }

    withType<ShadowJar> {
        dependencies {
            include(project(":tags-core"))
            include(project(":tags-api"))
        }

        archiveFileName.set(project.name + "aaaa.jar")
    }

    named("build") {
        dependsOn("writeDependenciesToFile", withType<ShadowJar>())
    }
}
