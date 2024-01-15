package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.CustomerApi
import esteban.g.facturacion.Api.ProductApi
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Entidades.ProductSend

object ProductLogic {
    suspend fun getListProduct(): MutableList<Product>? {
        return ProductApi.getListProduct()
    }

    suspend fun addProduct(productSend: ProductSend): Boolean {
        return ProductApi.addProduct(productSend)
    }

}