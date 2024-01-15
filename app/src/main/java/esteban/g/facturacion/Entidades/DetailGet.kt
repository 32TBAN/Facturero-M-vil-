package esteban.g.facturacion.Entidades
import com.google.gson.annotations.SerializedName
data class DetailGet (
    @SerializedName("iD_Orden")
    val id: Int,
    @SerializedName("iD_Producto")
    val idProduct: Int,
    @SerializedName("cantidad")
    val quantity: Int,
    @SerializedName("precio_Total")
    var price: Float,
    var name:String
)