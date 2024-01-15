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
import esteban.g.facturacion.Entidades.UserSend

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

    suspend fun getListUser(): List<User>? {
        return try {
            val response = service.getListUser()
            if (response.isSuccessful) {
                val userWrapper = response.body()
                return userWrapper?.usuarios
            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun addUser(userSend: UserSend?): Boolean {
        return try {
            if (userSend != null){
                val listSend: MutableList<UserSend> = mutableListOf()
                listSend.add(userSend)
                val response = service.addUser(listSend)
                response.isSuccessful
            }else{
                false
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            false
        }
    }
}

