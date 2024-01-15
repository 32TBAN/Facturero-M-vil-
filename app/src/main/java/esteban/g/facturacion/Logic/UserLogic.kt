package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.UserApi
import esteban.g.facturacion.Entidades.User
import esteban.g.facturacion.Entidades.UserSend

object UserLogic {
    suspend fun getUserByUsernameAndPassword(username: String, password: String): User? {
        return try {
            UserApi.getUserByUsernameAndPassword(username, password)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getListUser(): List<User>? {
        return UserApi.getListUser()
    }

    suspend fun addUser(userSend: UserSend): Boolean {
        return UserApi.addUser(userSend)
    }
}


