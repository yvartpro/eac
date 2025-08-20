package bi.vovota.eac.data.repository

import android.util.Log
import bi.vovota.eac.data.model.TokenManager
import bi.vovota.eac.data.model.TokenResponse
import bi.vovota.eac.data.model.UserLogin
import bi.vovota.eac.data.model.UserRegister
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import kotlinx.coroutines.withTimeout
import javax.inject.Inject

class AuthRepository @Inject constructor(
  private val client: HttpClient
) {
  suspend fun registerUser(userRegister: UserRegister): Boolean {
    return try {
      val resp = withTimeout(10000) {
        client.post("https://trade.vovota.bi/api/user/") {
          contentType(ContentType.Application.Json)
          setBody(userRegister)
        }
      }
      if(resp.status == HttpStatusCode.Created || resp.status == HttpStatusCode.OK){
        true
      }else{
        val err = resp.bodyAsText()
        false
      }
    } catch (e: Exception) {
      e.printStackTrace()
      false
    }
  }

  suspend fun loginUser(phone: String, password: String): TokenResponse? {
    return try {
      val resp = client.post("https://trade.vovota.bi/api/token/") {
        contentType(ContentType.Application.Json)
        setBody(UserLogin(phone, password))
      }
      resp.body()
    } catch (e: Exception) {
      e.printStackTrace()
      null
    }
  }

  suspend fun refreshToken(): Boolean {
    val refresh = TokenManager.getRefreshToken()
    if (refresh.isNullOrEmpty()) {
      println("No refresh token available")
      return false
    }
    return try {
        val response = client.post("https://trade.vovota.bi/refresh") {
          contentType(ContentType.Application.Json)
          setBody(mapOf("refresh" to refresh))
        }
      val tokenResponse = response.body<TokenResponse>()
      println("Token refreshed: ${tokenResponse.access}")
      TokenManager.saveTokens(tokenResponse.access, tokenResponse.refresh)
      true
    } catch (e: Exception) {
      println("Refresh failed: ${e.message}")
      TokenManager.clearTokens()
      false
    }
  }
}
