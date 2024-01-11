package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.Response
import retrofit2.create
import androidx.lifecycle.lifecycleScope

object UserApi {
    private val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)

    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return try {
            val response = service.getUserList()
            if (response.isSuccessful) {
                val userWrapper = response.body()

                val user = userWrapper?.usuarios?.firstOrNull {
                    println("Comparando: ${it.email} con $username y ${it.password} con $password")
                    it.email.equals(username, ignoreCase = true) && it.password.equals(password, ignoreCase = true)
                }
                return  user
            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }
}

