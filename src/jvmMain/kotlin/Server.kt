import plugins.configureCors
import plugins.configureRouting
import plugins.configureSerialization
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.application.*

fun main() {
    embeddedServer(
        Netty,
        port = 9090,
        host = "0.0.0.0",
        module = Application::module,
    ).start(wait = true)
}

fun Application.module() {
    configureCors()
    configureRouting()
    configureSerialization()
}


