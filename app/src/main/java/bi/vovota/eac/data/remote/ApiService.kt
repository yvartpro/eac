package bi.vovota.eac.data.remote

import bi.vovota.eac.data.remote.dto.AuthLogin
import bi.vovota.eac.data.remote.dto.AuthRegister
import bi.vovota.eac.data.remote.dto.AuthResponse
import bi.vovota.eac.data.remote.dto.ProfileResponse
import bi.vovota.eac.model.Product
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.serialization.json.Json

class ApiService(private val client: HttpClient) {
    val baseUrl = "https://air.vovota.bi/api"

    suspend fun register(request: AuthRegister) {
        client.post("$baseUrl/user/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }
    }

    suspend fun login(request: AuthLogin): AuthResponse {
        return client.post("$baseUrl/login/") {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()
    }

    suspend fun getProfile(token: String): List<ProfileResponse> {
        return client.get("$baseUrl/profile/") {
            header(HttpHeaders.Authorization, "Bearer $token")
        }.body()
    }

    suspend fun getProducts(): List<Product> {
        return try {
            println("🔎 Calling Product API...")
            val response: HttpResponse = client.get("https://mib.vovota.bi/api/product/")
            println("✅ Response: ${response.status}")
            val rawJson = response.bodyAsText()
            println("📦 Body: $rawJson")

            val products = Json.decodeFromString<List<Product>>(rawJson)
            println("✅ Products loaded: ${products.size}")
            products
        } catch (e: Exception) {
            println("❌ Exception: ${e.message}")
            emptyList()
        }
    }
}