package esteban.g.facturacion.Api

import com.google.gson.annotations.SerializedName
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Detail
import esteban.g.facturacion.Entidades.DetailGet
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Entidades.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("ListarUsuarios")
    suspend fun getUserList(): Response<UserWrapper>
    @GET("Listar")
    suspend fun getBillList(): Response<BillWrapper>
    @GET("ListarClientes")
    suspend fun getCustomerList(): Response<CustomerWrapper>
    @GET("ListarProductos")
    suspend fun getProductList(): Response<ProductWapper>
    @Headers("Content-Type: application/json;")
    @POST("Guardar")
    suspend fun  addBill(@Body bill: Bill): Response<Void>
    @Headers("Content-Type: application/json;")

    @POST("GuardarDetalle")
    suspend fun addDetails(@Body listDetails: List<Detail>): Response<Void>
    @DELETE("EliminarOrdenVenta")
    suspend fun deleteBill(@Query("id") id: Int): Response<Void>
    @GET("ListarDetalleOrden")
    suspend fun getListDetails(@Query("id") id: Int): Response<DetailWapper>
}

data class UserWrapper(
    @SerializedName("usuarios")
    val usuarios: List<User>
)

data class BillWrapper(
    @SerializedName("orden")
    val bill: List<Bill>
)

data class CustomerWrapper(
    @SerializedName("clientes")
    val customer: List<Customer>
)

data class ProductWapper(
    @SerializedName("producto")
    val product: MutableList<Product>
)
data class DetailWapper(
    @SerializedName("detalle")
    val detail: List<DetailGet>
)