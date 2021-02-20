dependencies {
    api("com.google.inject", "guice", Versions.GUICE)

    api("net.kyori", "adventure-api", Versions.ADVENTURE)
    api("net.kyori", "adventure-text-minimessage", Versions.MINI_MESSAGE) {
        isTransitive = true
    }
}
