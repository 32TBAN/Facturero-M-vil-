package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.UserApi
import esteban.g.facturacion.Entidades.User

object UserLogic {
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return try {
            UserApi.getUserByUsernameAndPassword(username, password)
        } catch (e: Exception) {
            null
        }
    }
}


