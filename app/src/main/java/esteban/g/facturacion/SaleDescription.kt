package esteban.g.facturacion

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.ClienteAdapter
import esteban.g.facturacion.Adapters.ProductAdapter
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Logic.CustomerLogic
import esteban.g.facturacion.Logic.ProductLogic
import kotlinx.coroutines.launch

class SaleDescription : AppCompatActivity(), ClienteAdapter.OnClienteSelectedListener, ProductAdapter.OnProductSelectedListener {
    private lateinit var customerAdapter: ClienteAdapter
    private var editCedula: TextView? = null
    private var editNombre: TextView? = null
    private var textViewAdrress: TextView? = null
    private var dialog:Dialog? = null
    private var selectedProducts: MutableList<Product> = mutableListOf()

    private lateinit var productAdapter: ProductAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sale_description)

        val btnSearchCustomer = findViewById<Button>(R.id.btnBuscarCliente)
        editCedula = findViewById(R.id.editCedula)
        editNombre = findViewById(R.id.editNombre)
        textViewAdrress = findViewById(R.id.textViewAdrressSelecd)

        val btnSearchProduct = findViewById<Button>(R.id.btnAddProduct)

        btnSearchCustomer.setOnClickListener {
            dialog = Dialog(this@SaleDescription)
            dialog!!.setContentView(R.layout.dialog_buscar_cliente)
            val editSearchCustomer = dialog!!.findViewById<EditText>(R.id.editTextBuscarCliente)

            lifecycleScope.launch {
                val recyclerViewClientes: RecyclerView = dialog!!.findViewById(R.id.recyclerViewClientes)
                val listCustomer: List<Customer>? = CustomerLogic.getListCustomer()
                if (!listCustomer.isNullOrEmpty()) {
                    customerAdapter = ClienteAdapter(listCustomer, this@SaleDescription)
                    recyclerViewClientes.adapter = customerAdapter
                    recyclerViewClientes.layoutManager = LinearLayoutManager(dialog!!.context)
                    dialog!!.show()

                    editSearchCustomer.addTextChangedListener {
                        filterCustomer(it.toString(),listCustomer)
                    }
                }
            }

        }

        btnSearchProduct.setOnClickListener{
            dialog = Dialog(this@SaleDescription)
            dialog!!.setContentView(R.layout.dialog_search_products)
            val editSearchProducts = dialog!!.findViewById<EditText>(R.id.editTextSearchProduct)

            lifecycleScope.launch {
                val recyclerViewProducts: RecyclerView = dialog!!.findViewById(R.id.recyclerViewProducts)
                val listProduct: List<Product>? = ProductLogic.getListProduct()
                if (!listProduct.isNullOrEmpty()) {
                    productAdapter = ProductAdapter(listProduct, this@SaleDescription)
                    recyclerViewProducts.adapter = productAdapter
                    recyclerViewProducts.layoutManager = LinearLayoutManager(dialog!!.context)
                    dialog!!.show()

                    editSearchProducts.addTextChangedListener {
                        filterProduct(it.toString(),listProduct)
                    }
                }
            }
        }

        val recyclerViewProductsAdd: RecyclerView = findViewById(R.id.recyclerViewProductsAdd)
        productAdapter = ProductAdapter(selectedProducts, this@SaleDescription)
        recyclerViewProductsAdd.adapter = productAdapter
        recyclerViewProductsAdd.layoutManager = LinearLayoutManager(this@SaleDescription)

    }

    private fun filterProduct(query: String, listProduct: List<Product>) {
        val listFilter = listProduct.filter {
            it.name.contains(query, ignoreCase = true)
        }
        productAdapter.updateList(listFilter ?: listProduct)
    }

    private fun filterCustomer(query: String, listCustomer: List<Customer>) {
        val listFilter = listCustomer.filter {
            it.identification.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.lastname.contains(query, ignoreCase = true)
        }
        customerAdapter.updateList(listFilter ?: listCustomer)
    }

    override fun onClienteSelected(cliente: Customer) {
        editCedula?.setText(cliente.identification)
        editNombre?.setText(cliente.name+" "+cliente.lastname)
        textViewAdrress?.setText(cliente.address)
        dialog?.dismiss()
    }

    override fun onProductSelected(product: Product) {
        selectedProducts.add(product)
        productAdapter.updateList(selectedProducts)
        dialog?.dismiss()
    }

}