dependencies {
    api("com.google.inject", "guice", Versions.GUICE)

    api("net.kyori", "adventure-api", Versions.ADVENTURE)
    api("net.kyori", "adventure-text-minimessage", Versions.ADVENTURE) {
        isTransitive = true
    }

    api("net.kyori", "event-api", Versions.EVENT)
}
