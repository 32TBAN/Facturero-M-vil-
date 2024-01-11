package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class Bill (
    @SerializedName("iD_Orden")
    val id: Int,
    @SerializedName("fecha")
    val date: String,
    @SerializedName("iD_Cliente")
    val idCustomer: String,
    @SerializedName("iD_Usuario")
    val idUser: String,
    @SerializedName("subtotal")
    val subtotal: String,
    @SerializedName("total")
    val total: String
)