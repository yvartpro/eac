package bi.vovota.eac.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//@Serializable
//data class User(
//    @SerialName("full_name") val name: String,
//    @SerialName("phone_number") val phone: String
//)

@Serializable
data class User(
    val id: Int,
    val fullName: String,
    val phone: String,
    val address: String? = null,
    val photo: String? = null,
) {
    val code: String = phone.take(3)
}