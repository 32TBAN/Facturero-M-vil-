package esteban.g.facturacion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.ProductAdapter
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Entidades.ProductSend
import esteban.g.facturacion.Logic.ProductLogic
import kotlinx.coroutines.launch

class ProductFragment : Fragment(), ProductAdapter.OnProductSelectedListener {
    private var userId: Int? = null
    private var userJob: String? = null
    private lateinit var productAdapter: ProductAdapter
    private var listProducts: List<Product>? = null

    companion object {
        fun newInstance(userId: Int?, userJob: String?): ProductFragment {
            val fragment = ProductFragment()
            val args = Bundle()
            if (userId != null) {
                args.putInt("userId", userId)
                args.putString("userJob",userJob)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragmeny_product, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            userId = it.getInt("userId")
            userJob = it.getString("userJob")
        }
        val btnAddProduct = view.findViewById<Button>(R.id.btnAgregarProduct)
        val searchProduct = view.findViewById<EditText>(R.id.editTextSearchProduct)
        btnAddProduct.visibility = if (userJob.equals("Ninguno", ignoreCase = true)) View.GONE else View.VISIBLE

        lifecycleScope.launch {
            listProducts = ProductLogic.getListProduct()
            if (!listProducts.isNullOrEmpty()) {
                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewProduct)
                productAdapter = ProductAdapter(listProducts!!,this@ProductFragment)
                recyclerView.adapter = productAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        searchProduct.addTextChangedListener { query ->
            var q = query.toString()
            if (q != "") {
                val listFilter = listProducts?.filter {
                    it.id.toString().contains(q, ignoreCase = true) ||
                            it.name.contains(q, ignoreCase = true) ||
                            it.stock.toString().contains(q, ignoreCase = true)
                }
                productAdapter.updateList(listFilter!!)
            }
        }

        btnAddProduct.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.modal_add_product, null)
            dialogBuilder.setView(dialogView)

            val etName = dialogView.findViewById<EditText>(R.id.nameEditText)
            val etDescription = dialogView.findViewById<EditText>(R.id.descriptionEditText)
            val etPrice = dialogView.findViewById<EditText>(R.id.priceEditText)
            val etStock = dialogView.findViewById<EditText>(R.id.stockEditText)
            val btnCancel = dialogView.findViewById<Button>(R.id.cancelButton)
            val btnSave = dialogView.findViewById<Button>(R.id.saveButton)

            val alertDialog = dialogBuilder.create()

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            btnSave.setOnClickListener {
                val name = etName.text.toString()
                val description = etDescription.text.toString()
                val price = etPrice.text.toString()
                val stock = etStock.text.toString()

                if (name.isEmpty() || description.isEmpty() || price.isEmpty() || stock.isEmpty()) {
                    showToast("Error, debe completar todos los campos.")
                } else {
                    val productSend = ProductSend(
                        name = name,
                        description = description,
                        price = price.toFloat(),
                        stock = stock.toInt()
                    )
                    lifecycleScope.launch {
                        if (ProductLogic.addProduct(productSend)) {
                            showToast("Enviando....")
                            alertDialog.dismiss()
                            listProducts = ProductLogic.getListProduct()
                            listProducts?.let { it1 -> productAdapter.updateList(it1) }
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

    override fun onProductSelected(product: Product) {
        println(product)
    }
}
