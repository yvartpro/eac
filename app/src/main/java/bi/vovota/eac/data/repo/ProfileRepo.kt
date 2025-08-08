package bi.vovota.eac.data.repo

import bi.vovota.eac.data.remote.dto.ProfileResponse

interface ProfileRepo {
    suspend fun getProfile(token: String): Result<ProfileResponse>
}