package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class User (
        @SerializedName("id")
        val id: Int,
        @SerializedName("cédula")
        val cedula: String,
        val nombre: String,
        @SerializedName("dirección")
        val direccion: String,
        @SerializedName("teléfono")
        val telefono: String,
)