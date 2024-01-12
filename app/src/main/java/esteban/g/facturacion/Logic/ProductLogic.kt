package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.CustomerApi
import esteban.g.facturacion.Api.ProductApi
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Product

object ProductLogic {
    suspend fun getListProduct(): List<Product>? {
        return ProductApi.getListProduct()
    }

}