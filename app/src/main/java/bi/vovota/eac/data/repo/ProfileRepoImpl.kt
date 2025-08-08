package bi.vovota.eac.data.repo

import bi.vovota.eac.data.remote.ApiService
import bi.vovota.eac.data.remote.dto.ProfileResponse

class ProfileRepoImpl( private val api: ApiService): ProfileRepo {
    override suspend fun getProfile(token: String): Result<ProfileResponse> {
        return try {
            val response = api.getProfile(token)
            Result.success(response[0])
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}