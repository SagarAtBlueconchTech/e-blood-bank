package com.example.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.routing.post
import io.ktor.server.util.*
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

        //-------------------- REST API ---------------------

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
        }



        //-------------------------- Calls from front-end -------------------------

        // --------- search hospital or blood bank
        get("/user/searchBank") {
            val hospitalList = hospitalService.getAll()
            call.respond(FreeMarkerContent("hospitalList.ftl",
                mapOf("hospitalList" to hospitalList, "requestUrl" to call.request.uri)))
        }

        post("/user/searchHospital") {
            val formParameters = call.receiveParameters()
            val city = formParameters.getOrFail("city")
            val bloodGroup = formParameters.getOrFail("bloodGroup")

            val hospitalList = hospitalService.searchHospitalsByBloodGroupAndCity(bloodGroup, city)
            call.respond(FreeMarkerContent("hospitalList.ftl",
                mapOf("hospitalList" to hospitalList, "requestUrl" to call.request.uri)))
        }

        //------------ New Hospital or blood bank >>>>> Dashboard
        get("/hospital/newHospital") {
            call.respond(FreeMarkerContent("newHospital.ftl",
                mapOf("requestUrl" to call.request.uri)))
        }

        post("/hospital/saveUpdateHospital") {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val email = formParameters.getOrFail("email")
            val phone = formParameters.getOrFail("address")
            val address = formParameters.getOrFail("phone")
            val bloodGroup = formParameters.getAll("bloodGroup")

            val newHospital = Hospital(name, address, email, phone, bloodGroup.toString())
            val id = hospitalService.create(newHospital)
            call.respond(FreeMarkerContent("newHospital.ftl",
                mapOf("requestUrl" to call.request.uri)))
        }


    }
}