package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.User
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response
class UserApi {
    val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val userService: ListaUsers
    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(urlBase)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        userService = retrofit.create(ListaUsers::class.java)
    }
    interface ListaUsers{
        @GET("/ListarUsuarios")
        suspend fun getUsers(): Response<List<User>>
    }

    suspend fun fetchUsers(): List<User> {
        return try {
            val response = userService.getUsers()
            if (response.isSuccessful) {
                response.body() ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return try {
            val response = userService.getUsers()
            if (response.isSuccessful) {
                val users = response.body() ?: emptyList()
                users.find { it.username == username && it.password == password }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}