package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.Bill
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object BillApi {
    private val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)
    suspend fun listaFacturas(): List<Bill>? {
        return try {
            val response = service.getBillList()
            if (response.isSuccessful) {
                val billWrapper = response.body()
                return billWrapper?.bill
            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }
}