package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.BillApi
import esteban.g.facturacion.Entidades.Bill

object BillLogic {
        suspend fun listaFacturas(): List<Bill>? {
            return BillApi.listaFacturas();
        }
}