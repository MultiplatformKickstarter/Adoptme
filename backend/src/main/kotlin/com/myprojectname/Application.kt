package com.myprojectname

import com.myprojectname.plugins.configureAdministration
import com.myprojectname.plugins.configureHTTP
import com.myprojectname.plugins.configureMonitoring
import com.myprojectname.plugins.configureSerialization
import com.myprojectname.plugins.configureTemplating
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
