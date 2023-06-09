package com.example.plugins

import LoginService
import LoginUser
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.freemarker.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.util.*
import org.jetbrains.exposed.sql.Database

fun Application.configureLoginRouting() {
    val database = Database.connect(
        url = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1",
        user = "root",
        driver = "org.h2.Driver",
        password = ""
    )

    val loginService = LoginService(database)

    routing {
        // ---------- Register ---------
        post("/registerApi") {
            val loginUser = call.receive<LoginUser>()
            val id = loginService.create(loginUser)
            call.respond(HttpStatusCode.Created, id)
        }

        post("/register") {
            val formParameters = call.receiveParameters()
            val email = formParameters.getOrFail("email")
            val password = formParameters.getOrFail("password")
            val userType = formParameters.getOrFail("usertype")

            val loginUser = LoginUser(null, email, password, userType)
            val id = loginService.create(loginUser)
            if(userType.equals("user", true)) {
                call.respondRedirect("/user/dashboard")
            } else {
                call.respondRedirect("/hospital/newHospital")
            }
        }


        // ---------- Login ---------
        get("/loginApi") {
            val email: String? = call.request.queryParameters["email"]
            val password: String? = call.request.queryParameters ["password"]
            val usertype: String? = call.request.queryParameters["usertype"]
            println(">>>>>>>>> $email | $password")
            val loginUser = loginService.validateCredentials(email, password, usertype)
            if (loginUser != null) {
                call.respond(HttpStatusCode.OK, loginUser)
            } else {
                call.respond(HttpStatusCode.NotFound, "No login user found")
            }
        }

        post("/login") {
            val formParameters = call.receiveParameters()
            val email = formParameters.getOrFail("email")
            val password = formParameters.getOrFail("password")
            val userType = formParameters.getOrFail("usertype")
            println(">>>>>>>>> $email | $password | $userType")
            val loginUser = loginService.validateCredentials(email, password, userType)
            if (loginUser != null) {
                if (loginUser.userType.equals("user", true)) {
                    call.respondRedirect("/user/dashboard")
                } else if (loginUser.userType.equals("hospital", true)) {
                    call.respondRedirect("/hospital/newHospital")
                } else {
                    call.respond(FreeMarkerContent("homePageContent.ftl", model = null))
                }
                //call.respond(HttpStatusCode.OK, loginUser)

            } else {
                call.respond(FreeMarkerContent("homePageContent.ftl", model = null))
            }
        }

    }

}