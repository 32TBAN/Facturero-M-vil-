package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.UserApi
import esteban.g.facturacion.Entidades.User

class UserLogic {
    private val userApi = UserApi()
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return try {
            userApi.getUserByUsernameAndPassword(username,password)
        } catch (e: Exception) {
            null
        }
    }
}