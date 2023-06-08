package com.example

import io.ktor.server.application.*
import com.example.plugins.*
import io.ktor.server.auth.*
import io.ktor.server.engine.*
import io.ktor.server.freemarker.*
import io.ktor.server.netty.*

fun main(args: Array<String>): Unit =
    io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // application.conf references the main function. This annotation prevents the IDE from marking it as unused.
fun Application.module() {

    install(Authentication) {
        basic("auth-basic") {
            realm = "Access to the '/' path"
            validate { credentials ->
                if (credentials.name == "test" && credentials.password == "test") {
                    UserIdPrincipal(credentials.name)
                } else {
                    null
                }
            }
        }
    }

    configureSerialization()
    configureDatabases()
    configureHospitalsRouting()
    configureLoginRouting()
    configureHTTP()
    configureMonitoring()
    configureRouting()
    configureTemplating()


}
