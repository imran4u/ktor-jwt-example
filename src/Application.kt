package com.imran

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.auth.*
import com.fasterxml.jackson.databind.*
import jwtConfig.JwtConfig
import model.User
import io.ktor.auth.jwt.jwt
import io.ktor.jackson.*
import io.ktor.features.*
import user

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@Suppress("unused") // Referenced in application.conf
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {

    install(Authentication) {
        jwt {
            verifier(JwtConfig.verifier)
            realm = "com.imran"
            validate {
                val name = it.payload.getClaim("name").asString()
                val password = it.payload.getClaim("password").asString()
                if(name != null && password != null){
                    User(name, password )
                }else{
                    null
                }
            }
        }
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    routing {
        get("/") {
            call.respondText("HELLO WORLD!", contentType = ContentType.Text.Plain)
        }

        get("/json/jackson") {
            call.respond(mapOf("hello" to "world"))
        }

        post("/generate_token"){
            val user = call.receive<User>()
            print("${user.name} , pwd= ${user.password}")
            val token = JwtConfig.generateToken(user)
            call.respond(token)

        }

        authenticate{
            get("/authenticate"){
                call.respond("get authenticated value from token " +
                        "name = ${call.user?.name}, password= ${call.user?.password}")
            }
        }
    }
}

