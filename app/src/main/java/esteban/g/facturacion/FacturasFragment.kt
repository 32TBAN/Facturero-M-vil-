package esteban.g.facturacion

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.BillAdapterShow
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Logic.BillLogic
import esteban.g.facturacion.Logic.CustomerLogic
import esteban.g.facturacion.Logic.ProductLogic
import kotlinx.coroutines.launch

class FacturasFragment : Fragment(), BillAdapter.OnBillSelectedListener {
    companion object {
        fun newInstance(userId: Int?): FacturasFragment {
            val fragment = FacturasFragment()
            val args = Bundle()
            if (userId != null) {
                args.putInt("userId", userId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    private var userId: Int? = null
    private val ADD_BILL_REQUEST_CODE = 1
    private var listBills: List<Bill>? = null
    private lateinit var billAdapter: BillAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facturas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val buttonAddBill = view.findViewById<Button>(R.id.btnAgregarFactura)
        val searchBills = view.findViewById<EditText>(R.id.editTextSearchFac)

        arguments?.let {
            userId = it.getInt("userId")
        }

        buttonAddBill.setOnClickListener {
            val intent = Intent(requireContext(), SaleDescription::class.java).apply {
                putExtra("userId", userId)
            }
            startActivityForResult(intent, ADD_BILL_REQUEST_CODE)
        }

        lifecycleScope.launch {
            listBills = BillLogic.listaFacturas();

            if (!listBills.isNullOrEmpty()) {
                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFacturas)
                billAdapter = BillAdapter(listBills!!, this@FacturasFragment)
                recyclerView.adapter = billAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        searchBills.addTextChangedListener { query ->
            var q = query.toString()
            if (q != "") {
                val listFilter = listBills?.filter {
                    it.id.toString().contains(q, ignoreCase = true) ||
                            it.date.contains(q, ignoreCase = true)
                }
                billAdapter.updateList(listFilter!!)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ADD_BILL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // Actualizar la lista de facturas
            lifecycleScope.launch {
                val listBills: List<Bill>? = BillLogic.listaFacturas();

                if (!listBills.isNullOrEmpty()) {
                    val recyclerView: RecyclerView = view?.findViewById(R.id.recyclerViewFacturas)!!
                    val billAdapter = BillAdapter(listBills, this@FacturasFragment)
                    recyclerView.adapter = billAdapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
    }

    override fun onDeleteBillSelected(id: Int) {
        lifecycleScope.launch {
            if (BillLogic.deleteBill(id)) {

                Toast.makeText(
                    requireContext(),
                    "Factura $id eliminada",
                    Toast.LENGTH_SHORT
                ).show()
                listBills = listBills?.toMutableList()?.apply {
                    removeAll { it.id == id }
                }

                listBills?.let { billAdapter.updateList(it) }
            } else {
                Toast.makeText(
                    requireContext(),
                    "Error al eliminar",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    override fun showBill(id: Int) {
        lifecycleScope.launch {
            listBills = BillLogic.listaFacturas()
        }
        val selectedBill = listBills?.find { it.id == id }

        if (selectedBill != null) {
            val inflater = LayoutInflater.from(requireContext())
            val modalView = inflater.inflate(R.layout.layout_modal_bill, null)

            val builder = AlertDialog.Builder(requireContext())
            builder.setView(modalView)
            val alertDialog = builder.create()

            val titleTextView: TextView = modalView.findViewById(R.id.modal_title)
            val fechaTextView: TextView = modalView.findViewById(R.id.textFecha)
            val clienteTextView: TextView = modalView.findViewById(R.id.textCliente)
            val subtotalTextView: TextView = modalView.findViewById(R.id.textSubtotal)
            val totalTextView: TextView = modalView.findViewById(R.id.textTotal)

            // Establecer los textos con los datos de la factura
            titleTextView.text = "Factura #${selectedBill.id}"
            fechaTextView.text = "Fecha: ${selectedBill.date}"
            lifecycleScope.launch {
                for (customer in CustomerLogic.getListCustomer()!!) {
                    if (customer.id == selectedBill.idCustomer) {
                        clienteTextView.text = "Cliente: ${customer.name} ${customer.lastname}"
                        break
                    }
                }
                var total =0.0
                val listDetails = BillLogic.getListDetails(selectedBill.id)
                val lisProduct = ProductLogic.getListProduct()
                for (details in listDetails!!) {
                    total += details.price
                    for (product in lisProduct!!) {
                        if (details.idProduct == product.id) {
                            details.name = product.name
                            details.price = product.price
                            break
                        }
                    }
                }
                val recyclerViewDetails: RecyclerView =
                    modalView.findViewById(R.id.recyclerViewProductsShow)
                val adapter = BillAdapterShow(listDetails)
                recyclerViewDetails.adapter = adapter
                recyclerViewDetails.layoutManager = LinearLayoutManager(requireContext())
                subtotalTextView.text = (selectedBill.subtotal*0.88).toString()
                totalTextView.text = String.format("%.2f",total)
            }

            val closeButton = modalView.findViewById<View>(R.id.btnCloseModal)
            closeButton.setOnClickListener {
                alertDialog.dismiss()
            }

            alertDialog.show()
        } else {
            Toast.makeText(
                requireContext(),
                "No se encontró la factura con el ID $id",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}