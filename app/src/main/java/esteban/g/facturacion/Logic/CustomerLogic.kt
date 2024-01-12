package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.CustomerApi
import esteban.g.facturacion.Entidades.Customer

object CustomerLogic {
    suspend fun getListCustomer(): List<Customer>? {
        return CustomerApi.getListCustomer()
    }

}