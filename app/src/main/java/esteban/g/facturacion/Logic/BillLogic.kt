package esteban.g.facturacion.Logic

import esteban.g.facturacion.Api.BillApi
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Detail
import esteban.g.facturacion.Entidades.DetailGet

object BillLogic {
    suspend fun listaFacturas(): List<Bill>? {
        return BillApi.listaFacturas();
    }

    suspend fun getNumBill(): Int {
        return BillApi.getNumBill()
    }

    suspend fun addBill(bill: Bill?): Boolean {
        return BillApi.addBill(bill)
    }

    suspend fun addDetails(listDetails: MutableList<Detail>): Boolean {
        return BillApi.addDetails(listDetails)
    }

    suspend fun deleteBill(id: Int): Boolean {
        return BillApi.deleteBill(id)
    }

    suspend fun getListDetails(id:Int): List<DetailGet>? {
        return BillApi.getListDetails(id)
    }

}