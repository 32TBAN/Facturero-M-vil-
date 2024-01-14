package esteban.g.facturacion.Entidades

import com.google.gson.annotations.SerializedName

data class Product (
    @SerializedName("iD_Producto")
    val id: Int,
    @SerializedName("nombre")
    val name: String,
    @SerializedName("precio")
    val price: Float,
    @SerializedName("stock")
    var stock: Int,
    var quantiy: Int = 1,
    var originalStock: Int
)