package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class ProductSend (
    @SerializedName("nombre")
    val name: String,
    @SerializedName("descripcion")
    val description: String,
    @SerializedName("precio")
    val price: Float,
    @SerializedName("stock")
    var stock: Int,
)