package routes

import common.RootPaths
import common.protocol.HeroesResponse
import db.DbService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.util.pipeline.*

fun Route.browserRoutes() {
    get("/") {
        indexHtml()
    }
    val paths = RootPaths().Heroes
    route(paths.relativePath) {
        get {
            indexHtml()
        }
        get(paths.New.relativePath) {
            indexHtml()
        }
        get("{id}") {
            indexHtml()
        }
    }
    route(RootPaths().Commit.relativePath) {
        get {
            indexHtml()
        }
    }
}

private suspend fun PipelineContext<Unit, ApplicationCall>.indexHtml() {
    call.respondText(
        this::class.java.classLoader.getResource("index.html")!!.readText(),
        ContentType.Text.Html
    )
}
