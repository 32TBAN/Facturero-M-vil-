package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class User (
        @SerializedName("iD_Usuario")
        val id: Int?,
        @SerializedName("email")
        val email: String,
        @SerializedName("password")
        val password: String,
        @SerializedName("cargo")
        val job: String
)