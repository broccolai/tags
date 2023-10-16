plugins {
    id("xyz.jpenilla.run-paper") version "2.2.0"
}

setupShadowJar()

dependencies {
    api(project(":tags-core"))

    compileOnly(libs.paper.api)
    compileOnly(libs.papi)
    compileOnly(libs.vault)

    api(libs.cloud.paper)
    api(libs.cloud.extras)

    implementation(libs.h2)
}

tasks {
    runServer {
        minecraftVersion("1.20")
    }
}
