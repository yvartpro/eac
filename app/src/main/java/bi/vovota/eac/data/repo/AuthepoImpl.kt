package bi.vovota.eac.data.repo

import bi.vovota.eac.data.remote.ApiService
import bi.vovota.eac.data.remote.dto.AuthLogin
import bi.vovota.eac.data.remote.dto.AuthRegister
import bi.vovota.eac.data.remote.dto.AuthResponse

class AuthRepositoryImpl( private val api: ApiService) : AuthRepo {

    override suspend fun register(request: AuthRegister): Result<Unit> {
        return try {
            api.register(request)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun login(request: AuthLogin): Result<AuthResponse> {
        return try {
            val response = api.login(request)
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}