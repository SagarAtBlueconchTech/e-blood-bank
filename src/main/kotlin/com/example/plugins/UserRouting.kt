package com.example.plugins

import org.jetbrains.exposed.sql.*
import io.ktor.http.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import kotlin.math.log

fun Application.configureDatabases() {
    val database = Database.connect(
            url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
            user = "root",
            driver = "org.h2.Driver",
            password = ""
        )
    val userService = UserService(database)
    routing {

        //-------------------- REST API ---------------------
        // Create user
        post("/users") {
            val user = call.receive<User>()
            val id = userService.create(user)
            call.respond(HttpStatusCode.Created, id)
        }

        // Read user
        get("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = userService.read(id)
            if (user != null) {
                call.respond(HttpStatusCode.OK, user)
            } else {
                call.respond(HttpStatusCode.NotFound)
            }
        }
        // Update user
        put("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            val user = call.receive<User>()
            userService.update(id, user)
            call.respond(HttpStatusCode.OK)
        }

        // Delete user
        delete("/users/{id}") {
            val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid ID")
            userService.delete(id)
            call.respond(HttpStatusCode.OK)
        }

        //Search user
        get("/users") {
            val bloodGroup : String? = call.request.queryParameters["bloodGroup"]
            val city : String? = call.request.queryParameters["city"]
            println(">>> parameters = $bloodGroup, $city")

            val userList  = if (bloodGroup.isNullOrBlank() && city.isNullOrBlank()) {
                userService.readAll()
            } else {
                userService.searchUsersByBloodGroupAndCity(bloodGroup, city)
            }

            if (userList.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No matching donor found")
            } else {
                call.respond(HttpStatusCode.OK, userList)
            }
        }




        //-------------------------- Calls from front-end -------------------------

        get("/user/dashboard") {
            val userList = userService.readAll()

            if (userList.isEmpty()) {
                call.respond(HttpStatusCode.NotFound, "No matching donor found")
            } else {
                //call.respond(HttpStatusCode.OK, userList)
                call.respond(FreeMarkerContent("userList.ftl",
                    mapOf("users" to userList, "requestUrl" to call.request.uri)))
            }
        }

        post("/user/searchUsers") {
            val formParameters = call.receiveParameters()
            val city = formParameters.getOrFail("city")
            val bloodGroup = formParameters.getOrFail("bloodGroup")

            val userList  = userService.searchUsersByBloodGroupAndCity(bloodGroup, city)
            call.respond(FreeMarkerContent("userList.ftl",
                mapOf("users" to userList, "requestUrl" to call.request.uri)))
        }


        get("/user/newUser") {
            call.respond(FreeMarkerContent("newUser.ftl", mapOf("requestUrl" to call.request.uri)))
        }

        post("/user/saveUpdateUser") {
            val formParameters = call.receiveParameters()
            val name = formParameters.getOrFail("name")
            val email = formParameters.getOrFail("email")
            val mobile = formParameters.getOrFail("mobile")
            val age = formParameters.getOrFail("age").toInt()
            val bloodGroup = formParameters.getOrFail("bloodGroup")
            val address = formParameters.getOrFail("address")
            //val loginUserId : Int? = formParameters.getOrFail("loginUserId").toInt()

            val newUser = User(name, email, mobile, age, bloodGroup, address, loginUserId = 0)
            val id = userService.create(newUser)
            call.respond(FreeMarkerContent("newUser.ftl", mapOf("requestUrl" to call.request.uri)))
        }

    }
}
