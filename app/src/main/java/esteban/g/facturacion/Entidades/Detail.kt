package esteban.g.facturacion.Entidades
import com.google.gson.annotations.SerializedName
data class Detail (
    @SerializedName("iD_Orden")
    val id: Int,
    @SerializedName("iD_Producto")
    val idProduct: Int,
    @SerializedName("cantidad")
    val quantity: Int,
    @SerializedName("precio_Total")
    val price: Float
)