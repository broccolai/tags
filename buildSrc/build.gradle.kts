plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("com.github.jengelman.gradle.plugins", "shadow", "6.1.0")
}
