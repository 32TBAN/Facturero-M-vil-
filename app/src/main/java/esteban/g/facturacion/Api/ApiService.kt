package esteban.g.facturacion.Api

import com.google.gson.annotations.SerializedName
import esteban.g.facturacion.Entidades.User
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("ListarUsuarios")
    suspend fun getUserList(): Response<UserWrapper>
}

data class UserWrapper(
    @SerializedName("usuarios")
    val usuarios: List<User>
)
