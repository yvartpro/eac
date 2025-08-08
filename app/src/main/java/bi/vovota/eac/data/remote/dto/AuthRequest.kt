package bi.vovota.eac.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AuthRegister(
    @SerialName("full_name") val name: String,
    val password: String
)

@Serializable
data class AuthLogin(
    @SerialName("phone_number") val phone: String,
    val password: String
)
