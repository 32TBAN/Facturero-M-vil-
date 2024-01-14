package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class Bill(
    @SerializedName("iD_Orden")
    var id: Int,
    @SerializedName("fecha")
    var date: String,
    @SerializedName("iD_Cliente")
    var idCustomer: Int,
    @SerializedName("iD_Usuario")
    var idUser: Int,
    @SerializedName("subtotal")
    var subtotal: Double,
    @SerializedName("total")
    var total: Double
)