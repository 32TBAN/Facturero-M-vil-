package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.CustomerApi
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.CustomerSend

object CustomerLogic {
    suspend fun getListCustomer(): List<Customer>? {
        return CustomerApi.getListCustomer()
    }

    suspend fun addCustomer(customerSend: CustomerSend): Boolean {
        return CustomerApi.addCustomer(customerSend)
    }

}