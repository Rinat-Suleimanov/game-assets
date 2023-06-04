package routes

import common.RootPaths
import common.model.HeroModel
import common.protocol.HeroCreateRequest
import common.protocol.HeroResponse
import common.protocol.HeroesResponse
import db.DbService
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardOpenOption
import java.util.*

fun Route.apiRoutes() {
    val paths = RootPaths().Api.V1
    route(paths.toString()) {
        route(paths.Heroes.relativePath) {
            get {
                val completed = call.parameters["completed"]
                call.respond(
                    when {
                        completed == null -> HeroesResponse(DbService.getAllHeroes())
                        completed.toBoolean() -> HeroesResponse(DbService.getCompletedHeroes())
                        else -> HeroesResponse(DbService.getUncompletedHeroes())
                    }
                )
            }
            get("{id}") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val hero = DbService.findHero(id)
                    if (hero == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(HeroResponse(hero))
                    }
                }
            }
            post {
                val createRequest = call.receive<HeroCreateRequest>()
                DbService.insertHero(createRequest.heroId)
                call.respond(HttpStatusCode.Created)
            }
            post("{id}/icon") {
                val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val multipartData = call.receiveMultipart()

                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val randomPart = UUID.randomUUID()
                            val extension = (part.originalFileName as String).substringAfterLast(".")
                            val fileBytes = part.streamProvider().readBytes()
                            Files.createDirectories(Path.of("$uploadsDirectory/$id"))
                            Files.write(
                                Path.of("$uploadsDirectory/$id/icon-$randomPart.$extension"),
                                fileBytes,
                                StandardOpenOption.CREATE
                            )
                            DbService.updateHeroIcon(id, "/uploads/$id/icon-$randomPart.$extension")
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                call.respond(HeroResponse(DbService.findHeroChanges(id)))
            }
            post("{id}/image") {
                val id = call.parameters["id"] ?: return@post call.respond(HttpStatusCode.BadRequest)
                val multipartData = call.receiveMultipart()

                multipartData.forEachPart { part ->
                    when (part) {
                        is PartData.FileItem -> {
                            val extension = (part.originalFileName as String).substringAfterLast(".")
                            val fileBytes = part.streamProvider().readBytes()
                            val randomPart = UUID.randomUUID()
                            Files.createDirectories(Path.of("$uploadsDirectory/$id"))
                            Files.write(
                                Path.of("$uploadsDirectory/$id/image-$randomPart.$extension"),
                                fileBytes,
                                StandardOpenOption.CREATE
                            )
                            DbService.updateHeroImage(id, "/uploads/$id/image-$randomPart.$extension")
                        }

                        else -> {}
                    }
                    part.dispose()
                }

                call.respond(HeroResponse(DbService.findHeroChanges(id)))
            }

            get("{id}/changes") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val hero = DbService.findHeroChanges(id)
                    if (hero == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(HeroResponse(hero))
                    }
                }
            }
            post("{id}/changes") {
                val id = call.parameters["id"]
                if (id == null) {
                    call.respond(HttpStatusCode.BadRequest)
                } else {
                    val hero = commitChanges(id)
                    if (hero == null) {
                        call.respond(HttpStatusCode.NotFound)
                    } else {
                        call.respond(HeroResponse(hero))
                    }
                }
            }

        }
    }
}

fun commitChanges(id: String): HeroModel? {
    val hero = DbService.findHero(id) ?: return null
    val heroChanges = DbService.findHeroChanges(id) ?: return hero

    val tmpIcon = heroChanges.headIcon
    val tmpImage = heroChanges.bigImage

    val newIconPath = tmpIcon?.let { moveFromTmp(it) }
    val newImagePath = tmpImage?.let { moveFromTmp(it) }

    val updatedHero = hero.apply {
        newIconPath?.let { headIcon = it }
        newImagePath?.let { bigImage = it }
    }
    DbService.updateHero(updatedHero)
    DbService.deleteHeroChanges(id)
    return updatedHero
}

private fun moveFromTmp(tmp: String): String {
    val relativeFilePath = tmp.removePrefix("/uploads")
    Files.createDirectories(Path.of("$assetsDirectory/${relativeFilePath.substringBeforeLast("/")}"))
    val tmpFile = File("$uploadsDirectory/$relativeFilePath")
    Files.move(tmpFile.toPath(), Path.of("$assetsDirectory/$relativeFilePath"))
    return "/assets/$relativeFilePath"
}
