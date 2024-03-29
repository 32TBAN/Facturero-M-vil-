package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Entidades.ProductSend
import esteban.g.facturacion.Entidades.UserSend
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ProductApi {
    private val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)
    suspend fun getListProduct(): MutableList<Product>? {
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

    suspend fun addProduct(productSend: ProductSend?): Boolean {
        return try {
            if (productSend != null){
                val listSend: MutableList<ProductSend> = mutableListOf()
                listSend.add(productSend)
                val response = service.addProduct(listSend)
                response.isSuccessful
            }else{
                false
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            false
        }
    }

}