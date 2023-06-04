package plugins

import common.RootPaths
import common.protocol.HeroesResponse
import db.DbService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import routes.*
import java.io.File

fun Application.configureRouting() {

    routing {
        browserRoutes()
        staticRoutes()
        apiRoutes()
    }
}




