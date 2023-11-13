package com.multiplatformkickstarter.app.platform

sealed class Environment(val name: String, val url: String)

sealed class ServerEnvironment(name: String, endpoint: String) : Environment(name, endpoint) {
    data object PRODUCTION : Environment("PRODUCTION", "http://multiplatformkickstarter.com")
    data object PREPRODUCTION : Environment("PREPRODUCTION", "http://pre.multiplatformkickstarter.com")
    // This IP represents the localhost of your computer through the emulator
    data object LOCALHOST : Environment("LOCALHOST", "http://10.0.2.2:8080")

    companion object {
        val environments = listOf(PREPRODUCTION, PRODUCTION, LOCALHOST)
    }
}
