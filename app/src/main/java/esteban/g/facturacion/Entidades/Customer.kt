package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class Customer(
    @SerializedName("id")
    val id: Int,
    @SerializedName("cédula")
    val identification: String,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("apellido")
    val lastname: String,
    @SerializedName("dirección")
    val address: String,
    @SerializedName("teléfono")
    val phone: String
)