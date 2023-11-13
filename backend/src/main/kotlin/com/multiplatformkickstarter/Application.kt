package com.multiplatformkickstarter

import com.multiplatformkickstarter.plugins.configureAdministration
import com.multiplatformkickstarter.plugins.configureHTTP
import com.multiplatformkickstarter.plugins.configureMonitoring
import com.multiplatformkickstarter.plugins.configureSerialization
import com.multiplatformkickstarter.plugins.configureTemplating
import io.ktor.server.engine.embeddedServer
import io.ktor.server.tomcat.Tomcat

fun main() {
    embeddedServer(Tomcat, port = 8080, host = "0.0.0.0") {
        configureSessions()
        configureGeneralRouting()
        configureAuthentication()
        configureHTTP()
        configureMonitoring()
        configureTemplating()
        configureSerialization()
        configureAdministration()
    }.start(wait = true)
}

const val API_VERSION = "/v1"
