package bi.vovota.eac.data.repository

import android.content.Context
import android.util.Log
import bi.vovota.eac.data.model.TokenManager
import bi.vovota.eac.data.model.User
import bi.vovota.eac.data.model.UserRaw
import bi.vovota.eac.data.model.UserUpdate
import bi.vovota.eac.data.model.toUser
import bi.vovota.eac.data.model.TokenResponse
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.client.request.patch
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject


class UserRepository @Inject constructor(
  private val client: HttpClient,
  @ApplicationContext private val context: Context,
  private val tokenManager:
  TokenManager
)
{
  suspend fun getProfile(): User? {
    val token = tokenManager.getAccessToken() ?: return null

    return try {
      val response = client.get("https://trade.vovota.bi/api/user/") {
        header(HttpHeaders.Authorization, "Bearer $token")
      }
      when (response.status) {
        HttpStatusCode.OK -> response.body<User>()
        HttpStatusCode.Unauthorized -> null
        else -> throw Exception("Unexpected error: ${response.status}")
      }
    }catch (e: Exception) {
      null
    }
  }

  suspend fun editUser(update: UserUpdate, userId: Int): Boolean {
    val access = TokenManager.getAccessToken() ?: throw UnAuthorizedException("No access token")
    return try {
      val response = client.patch("https://trade.vovota.bi/api/user/$userId/"){
        header(HttpHeaders.Authorization, "Bearer $access")
        contentType(ContentType.Application.Json)
        setBody(update)
      }
      response.status == HttpStatusCode.OK || response.status == HttpStatusCode.Accepted
    } catch (e: ClientRequestException) {
      false
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }


  private suspend fun refreshToken(): Boolean {
    val refresh = TokenManager.getRefreshToken()
    if (refresh.isNullOrEmpty()) {
      println("No refresh token available")
      return false
    }
    return try {
      val response = client.post("https://trade.vovota.bi/api/refresh/") {
        contentType(ContentType.Application.Json)
        setBody(mapOf("refresh" to refresh))
      }
      val tokenResponse = response.body<TokenResponse>()
      TokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
      true
    } catch (e: Exception) {
      TokenManager.clearTokens()
      false
    }
  }
}

class UnAuthorizedException(message: String): Exception(message)