package routes

import common.RootPaths
import common.protocol.HeroesResponse
import db.DbService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File

val assetsDirectory = System.getenv("ASSETS_DIRECTORY") ?: "/tmp/assets"
val uploadsDirectory = System.getenv("UPLOADS_DIRECTORY") ?: "/tmp/uploads"
 fun Route.staticRoutes() {
    static("/") {
        resources()
    }
    static("/static") {
        install(CachingHeaders) {
            options { call, outgoingContent ->
                io.ktor.http.content.CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 24 * 60 * 60))
            }
        }
        resources()
    }

    static("/assets") {
        install(CachingHeaders) {
            options { call, outgoingContent ->
                io.ktor.http.content.CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 30))
            }
        }
        staticRootFolder = File(assetsDirectory)
        files(".")
    }

    static("/uploads") {
        install(CachingHeaders) {
            options { call, outgoingContent ->
                io.ktor.http.content.CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 23 * 60 * 60))
            }
        }
        staticRootFolder = File(uploadsDirectory)
        files(".")
    }
}
