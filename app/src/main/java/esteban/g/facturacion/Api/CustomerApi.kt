package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.CustomerSend
import esteban.g.facturacion.Entidades.UserSend
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CustomerApi {
    private val urlBase = "http://facturacionapirestcgjl.somee.com/Orden/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(urlBase)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val service = retrofit.create(ApiService::class.java)
    suspend fun getListCustomer(): List<Customer>? {
        return try {
            val response = service.getCustomerList()
            if (response.isSuccessful) {
                val customerWrapper = response.body()
                return customerWrapper?.customer
            } else {
                null
            }
        } catch (e: Exception) {
            println(e.message)
            null
        }
    }

    suspend fun addCustomer(customerSend: CustomerSend?): Boolean {
        return try {
            if (customerSend != null){
                val listSend: MutableList<CustomerSend> = mutableListOf()
                listSend.add(customerSend)
                val response = service.addCustomer(listSend)
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