package esteban.g.facturacion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.ClienteAdapter
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.CustomerSend
import esteban.g.facturacion.Logic.CustomerLogic
import kotlinx.coroutines.launch

class CustomerFragment : Fragment(), ClienteAdapter.OnClienteSelectedListener {
    private var userId: Int? = null
    private lateinit var customerAdapter: ClienteAdapter
    private var listCustomers: List<Customer>? = null

    companion object {
        fun newInstance(userId: Int?): CustomerFragment {
            val fragment = CustomerFragment()
            val args = Bundle()
            if (userId != null) {
                args.putInt("userId", userId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmeny_customer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            userId = it.getInt("userId")
        }
        val btnAddCustomer = view.findViewById<Button>(R.id.btnAgregarCustomer)
        val searchCustomer = view.findViewById<EditText>(R.id.editTextSearchCustomer)

        lifecycleScope.launch {
            listCustomers = CustomerLogic.getListCustomer()
            if (!listCustomers.isNullOrEmpty()) {
                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewCustomer)
                customerAdapter = ClienteAdapter(listCustomers!!,this@CustomerFragment)
                recyclerView.adapter = customerAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        searchCustomer.addTextChangedListener { query ->
            var q = query.toString()
            if (q != "") {
                val listFilter = listCustomers?.filter {
                    it.id.toString().contains(q, ignoreCase = true) ||
                            it.name.contains(q, ignoreCase = true) ||
                            it.lastname.contains(q, ignoreCase = true) ||
                            it.address.contains(q, ignoreCase = true) ||
                            it.identification.contains(q, ignoreCase = true)
                }
                customerAdapter.updateList(listFilter!!)
            }
        }

        btnAddCustomer.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.modal_add_customer, null)
            dialogBuilder.setView(dialogView)

            val etIdentification = dialogView.findViewById<EditText>(R.id.identificationEditText)
            val etName = dialogView.findViewById<EditText>(R.id.nameEditText)
            val etLastname = dialogView.findViewById<EditText>(R.id.lastnameEditText)
            val etAddress = dialogView.findViewById<EditText>(R.id.addressEditText)
            val etPhone = dialogView.findViewById<EditText>(R.id.phoneEditText)
            val btnCancel = dialogView.findViewById<Button>(R.id.cancelButton)
            val btnSave = dialogView.findViewById<Button>(R.id.saveButton)

            val alertDialog = dialogBuilder.create()

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            btnSave.setOnClickListener {
                val identification = etIdentification.text.toString()
                val name = etName.text.toString()
                val lastname = etLastname.text.toString()
                val address = etAddress.text.toString()
                val phone = etPhone.text.toString()

                if (identification.isEmpty() || name.isEmpty() || lastname.isEmpty()) {
                    showToast("Error, debe completar al menos cédula, nombre y apellido.")
                } else {
                    val customerSend = CustomerSend(
                        identification = identification,
                        name = name,
                        lastname = lastname,
                        address = address,
                        phone = phone
                    )
                    lifecycleScope.launch {
                        if (CustomerLogic.addCustomer(customerSend)) {
                            showToast("Enviando....")
                            alertDialog.dismiss()
                            listCustomers = CustomerLogic.getListCustomer()
                            listCustomers?.let { it1 -> customerAdapter.updateList(it1) }
                        } else {
                            showToast("Error al agregar....")
                        }
                    }

                }
            }
            alertDialog.show()

        }
    }

    private fun showToast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }

    override fun onClienteSelected(cliente: Customer) {
        println(cliente)
    }
}
