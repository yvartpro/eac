package bi.vovota.eac.model

import kotlinx.serialization.Serializable

@Serializable
data class Profile(
    val id: Int,
    val user: User
)