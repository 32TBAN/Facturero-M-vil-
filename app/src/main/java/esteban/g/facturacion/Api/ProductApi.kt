package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.Product
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductApi {
    private val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)
    suspend fun getListProduct(): List<Product>? {
        return try {
            val response = service.getProductList()
            if (response.isSuccessful) {
                val userWrapper = response.body()
                return  userWrapper?.product
            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

}