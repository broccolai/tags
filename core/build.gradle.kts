dependencies {
    api(project(":tags-api"))
    compileOnlyApi("com.google.guava", "guava", Versions.GUAVA)
    compileOnly("net.luckperms", "api", Versions.LUCKPERMS)

    implementation("com.google.inject.extensions", "guice-assistedinject", Versions.GUICE) {
        isTransitive = false
    }

    api("org.jdbi", "jdbi3-core", Versions.JDBI)
    implementation("com.zaxxer", "HikariCP", Versions.HIKARI)
    implementation("org.flywaydb", "flyway-core", Versions.FLYWAY)

    implementation("org.spongepowered", "configurate-hocon", Versions.CONFIGURATE)

    api("broccolai.corn", "corn-core", Versions.CORN)

    api("cloud.commandframework", "cloud-core", Versions.CLOUD)
    api("net.kyori", "coffee-functional", Versions.COFFEE)

    api("net.kyori", "event-method-asm", Versions.EVENT)
}
