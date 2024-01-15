package esteban.g.facturacion.Api

import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Detail
import esteban.g.facturacion.Entidades.DetailGet
import retrofit2.Response
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

    suspend fun getNumBill(): Int {
        return try {
            val response: Response<BillWrapper> = service.getBillList()
            if (response.isSuccessful) {
                val billList = response.body()?.bill
                // Ordenar la lista de facturas por id de forma descendente
                val sortedBillList = billList?.sortedByDescending { it.id }

                // Verificar si hay facturas en la lista
                if (sortedBillList != null && sortedBillList.isNotEmpty()) {
                    // Tomar el número de factura de la factura más reciente
                    return sortedBillList[0].id + 1
                } else {
                    // Si no hay facturas, podrías devolver 1 o algún otro valor predeterminado
                    1
                }
            } else {
                // Si la solicitud no es exitosa, devolver algún valor predeterminado
                1
            }
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante la solicitud
            println(e.message)
            // Devolver algún valor predeterminado en caso de error
            1
        }
    }

    suspend fun addBill(bill: Bill?): Boolean {
        return try {
            if (bill != null){
                val response = service.addBill(bill)
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

    suspend fun addDetails(listDetails: MutableList<Detail>): Boolean {
        return try {
            val response = service.addDetails(listDetails)
            response.isSuccessful
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            false
        }
    }

    suspend fun deleteBill(id: Int): Boolean {
        return try {
            val response = service.deleteBill(id)
            response.isSuccessful
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            false
        }
    }

    suspend fun getListDetails(id: Int): List<DetailGet>? {
        return try {
            val response = service.getListDetails(id)
            if (response.isSuccessful) {
                val detailWrapper = response.body()
                detailWrapper?.detail
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            println(e.message)
            e.printStackTrace()
            emptyList()
        }

    }
}