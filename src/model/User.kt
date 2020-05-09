package model

import io.ktor.auth.Principal

data class User(val name: String, val password: String, val other:String="default"): Principal