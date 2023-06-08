package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import org.jetbrains.exposed.sql.Database

fun Application.configureHospitalsRouting() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    val hospitalService = HospitalService(database)

    routing {
        authenticate("auth-basic") {
            post("/hospitals") {
                val hospital = call.receive<Hospital>()
                val id = hospitalService.create(hospital)
                call.respond(HttpStatusCode.Created, id)
            }

            get("/hospitals") {
                val hospitalList = hospitalService.getAll()
                if (hospitalList.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(HttpStatusCode.OK, hospitalList)
                }
            }

            get("/hospitalDashboard") {
                val hospitalList = hospitalService.getAll()
                if (hospitalList.isEmpty()) {
                    call.respond(HttpStatusCode.NotFound)
                } else {
                    call.respond(HttpStatusCode.OK, hospitalList)
                }
            }

        }

    }
}