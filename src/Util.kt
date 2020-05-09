import model.User
import io.ktor.application.ApplicationCall
import io.ktor.auth.authentication

val ApplicationCall.user get() = authentication.principal<User>()
