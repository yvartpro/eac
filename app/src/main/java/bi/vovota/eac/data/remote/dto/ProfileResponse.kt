package bi.vovota.eac.data.remote.dto

import bi.vovota.eac.model.User
import kotlinx.serialization.Serializable

@Serializable
data class ProfileResponse(
    val id: Int,
    val user: User
)