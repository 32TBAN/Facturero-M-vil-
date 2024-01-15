package esteban.g.facturacion.Entidades
import com.google.gson.annotations.SerializedName

data class UserSend (
    @SerializedName("email")
    val email: String,
    @SerializedName("password")
    val password: String,
    @SerializedName("cargo")
    val job: String
)