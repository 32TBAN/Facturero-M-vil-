package esteban.g.facturacion

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.ClienteAdapter
import esteban.g.facturacion.Adapters.ProductAdapter
import esteban.g.facturacion.Adapters.ProductAdapterAdd
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Logic.CustomerLogic
import esteban.g.facturacion.Logic.ProductLogic
import kotlinx.coroutines.launch

class SaleDescription : AppCompatActivity(), ClienteAdapter.OnClienteSelectedListener, ProductAdapter.OnProductSelectedListener,
ProductAdapterAdd.OnProductSelectedListener{
    private lateinit var customerAdapter: ClienteAdapter
    private var editCedula: TextView? = null
    private var editNombre: TextView? = null
    private var textViewAdrress: TextView? = null
    private var dialog:Dialog? = null
    private var dialogProduct:Dialog? = null
    private var selectedProducts: MutableList<Product> = mutableListOf()
    private var listProduct: MutableList<Product> = mutableListOf()
    private var removeListProduct: MutableList<Product> = mutableListOf()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var productAdapterAdd: ProductAdapterAdd
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

        dialogProduct = Dialog(this@SaleDescription)
        dialogProduct!!.setContentView(R.layout.dialog_search_products)
        val editSearchProducts = dialogProduct!!.findViewById<EditText>(R.id.editTextSearchProduct)

        lifecycleScope.launch {
            val recyclerViewProducts: RecyclerView = dialogProduct!!.findViewById(R.id.recyclerViewProducts)
            listProduct = ProductLogic.getListProduct()!!
            removeListProduct = listProduct
            if (!listProduct.isNullOrEmpty()) {
                productAdapter = ProductAdapter(listProduct, this@SaleDescription)
                recyclerViewProducts.adapter = productAdapter
                recyclerViewProducts.layoutManager = LinearLayoutManager(dialogProduct!!.context)

                editSearchProducts.addTextChangedListener {
                    filterProduct(it.toString(),listProduct)
                }
            }
        }
        btnSearchProduct.setOnClickListener{
            dialogProduct!!.show()
        }

        val recyclerViewProductsAdd: RecyclerView = findViewById(R.id.recyclerViewProductsAdd)
        productAdapterAdd = ProductAdapterAdd(selectedProducts, this@SaleDescription)
        recyclerViewProductsAdd.adapter = productAdapterAdd
        recyclerViewProductsAdd.layoutManager = LinearLayoutManager(this@SaleDescription)
    }

    private fun filterProduct(query: String, listProduct: MutableList<Product>) {
        val listFilter = listProduct.filter {
            it.name.contains(query, ignoreCase = true)
        }
        Toast.makeText(this,"a",Toast.LENGTH_SHORT).show()
        productAdapter.updateList(listFilter)
    }

    private fun filterCustomer(query: String, listCustomer: List<Customer>) {
        val listFilter = listCustomer.filter {
            it.identification.contains(query, ignoreCase = true) ||
                    it.name.contains(query, ignoreCase = true) ||
                    it.lastname.contains(query, ignoreCase = true)
        }
        customerAdapter.updateList(listFilter)
    }

    override fun onClienteSelected(cliente: Customer) {
        editCedula?.setText(cliente.identification)
        editNombre?.setText(cliente.name+" "+cliente.lastname)
        textViewAdrress?.setText(cliente.address)
        dialog?.dismiss()
    }

    //En este metodo primero se eliminar el producto que se va agregar en la lista de la factura
    //Posteriormente se agrega a la nueva lista de la factura
    override fun onProductSelected(product: Product) {
        removeListProduct.remove(product)
        productAdapter.updateList(removeListProduct)

        selectedProducts.add(product)
        productAdapterAdd.updateList(selectedProducts)
        dialogProduct?.dismiss()
    }
    //Lo contrario del metodo anterior
    override fun onProductSelectedAdd(product: Product) {
        selectedProducts.remove(product)
        productAdapterAdd.updateList(selectedProducts)

        removeListProduct.add(product)
        productAdapter.updateList(removeListProduct)
    }

}