import common.RootPaths
import common.protocol.HeroCreateRequest
import common.protocol.HeroResponse
import common.protocol.HeroesResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response
import org.w3c.files.File
import org.w3c.xhr.FormData

val jsonClient = HttpClient {
    install(ContentNegotiation) {
        json()
    }
}

val fileClient = HttpClient {

}

object HeroesApi {
    suspend fun fetchAllHeroes(): HeroesResponse {
        return jsonClient.get(RootPaths().Api.V1.Heroes.toString()) {
            accept(ContentType.Application.Json)
        }.body<HeroesResponse>()
    }

    suspend fun fetchNotCompletedHeroes(): HeroesResponse {
        return jsonClient.get(RootPaths().Api.V1.Heroes.toString()) {
            parameter("completed", "false")
            accept(ContentType.Application.Json)
        }.body<HeroesResponse>()
    }

    suspend fun fetchById(id: String): HeroResponse? {
        return try {
            jsonClient.get(RootPaths().Api.V1.Heroes.Id(id).toString()) {
                accept(ContentType.Application.Json)
            }.body<HeroResponse>()
        } catch (e: Exception) {
            console.log(e)
            null
        }
    }

    suspend fun fetchChangesById(id: String): HeroResponse? {
        return try {
            jsonClient.get(RootPaths().Api.V1.Heroes.Changes(id).toString()) {
                accept(ContentType.Application.Json)
            }.body<HeroResponse>()
        } catch (e: Exception) {
            console.log(e)
            null
        }
    }

    suspend fun createNewHero(createRequest: HeroCreateRequest) {
        jsonClient.post(RootPaths().Api.V1.Heroes.toString()) {
            contentType(ContentType.Application.Json)
            setBody(createRequest)
        }
    }

    suspend fun iconUpload(heroId: String, file: File): Response {
        val formData = FormData()
        formData.append(file.name, file)

        val response = window.fetch(
            RootPaths().Api.V1.Heroes.IconUpload(heroId).toString(),
            RequestInit(
                method = "POST",
                body = formData
            )
        ).await()
        return response
    }

    suspend fun imageUpload(heroId: String, file: File): Response {
        val formData = FormData()
        formData.append(file.name, file)

        val response = window.fetch(
            RootPaths().Api.V1.Heroes.ImageUpload(heroId).toString(),
            RequestInit(
                method = "POST",
                body = formData
            )
        ).await()
        return response
    }

    suspend fun commitChanges(id: String): HeroResponse {
        return jsonClient.post(RootPaths().Api.V1.Heroes.Changes(id).toString()) {
            contentType(ContentType.Application.Json)
        }.body<HeroResponse>()
    }
}
