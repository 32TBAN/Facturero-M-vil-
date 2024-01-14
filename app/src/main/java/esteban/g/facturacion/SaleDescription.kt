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
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Logic.BillLogic
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
    private var bill: Bill? = Bill(
        id = 0,
        date = "",
        idCustomer = "id_cliente",
        idUser = "id_usuario",
        subtotal = "0.0",
        total = "0.0"
    )
    private var billArticle: TextView? = null
    private var billDiscount: TextView? = null
    private var billIva: TextView? = null
    private var billTotal: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sale_description)

        val btnSearchCustomer = findViewById<Button>(R.id.btnBuscarCliente)
        editCedula = findViewById(R.id.editCedula)
        editNombre = findViewById(R.id.editNombre)
        textViewAdrress = findViewById(R.id.textViewAdrressSelecd)
        billArticle = findViewById(R.id.textArticlesEdit)
        billDiscount = findViewById(R.id.textDiscountEdit)
        billIva = findViewById(R.id.textIvaEdit)
        billTotal = findViewById(R.id.textTotalEdit)

        val btnSearchProduct = findViewById<Button>(R.id.btnAddProduct)
        //Abrir modal cliente
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
        //modal producto
        dialogProduct = Dialog(this@SaleDescription)
        dialogProduct!!.setContentView(R.layout.dialog_search_products)
        val editSearchProducts = dialogProduct!!.findViewById<EditText>(R.id.editTextSearchProduct)

        lifecycleScope.launch {
            val recyclerViewProducts: RecyclerView = dialogProduct!!.findViewById(R.id.recyclerViewProducts)
            listProduct = ProductLogic.getListProduct()!!
            removeListProduct = listProduct
            if (listProduct.isNotEmpty()) {
                productAdapter = ProductAdapter(listProduct, this@SaleDescription)
                recyclerViewProducts.adapter = productAdapter
                recyclerViewProducts.layoutManager = LinearLayoutManager(dialogProduct!!.context)

                editSearchProducts.addTextChangedListener {
                    filterProduct(it.toString(),listProduct)
                }
            }
            bill?.id  = BillLogic.getNumBill()
            if (bill?.id == 1){
                Toast.makeText(this@SaleDescription,"Error al obtener num. orden",Toast.LENGTH_SHORT).show()
            }
            val billId = findViewById<TextView>(R.id.textCodFacEdit)
            billId.text = bill?.id.toString()
        }
        btnSearchProduct.setOnClickListener{
            dialogProduct!!.show()
        }

        val recyclerViewProductsAdd: RecyclerView = findViewById(R.id.recyclerViewProductsAdd)
        productAdapterAdd = ProductAdapterAdd(selectedProducts, this@SaleDescription, ::updateBill)
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

        updateBill()
    }
    //Lo contrario del metodo anterior
    override fun onProductSelectedAdd(product: Product) {
        selectedProducts.remove(product)
        productAdapterAdd.updateList(selectedProducts)

        removeListProduct.add(product)
        productAdapter.updateList(removeListProduct)

        updateBill()
    }

    private fun updateBill() {
        var totalArticles = 0
        var totalDiscount = 0.0
        var totalIva = 0.0
        var totalAmount = 0.0

        for (product in selectedProducts) {
            totalArticles += product.quantiy
            totalDiscount += (product.price * product.quantiy * 0.12)
            totalIva += (product.price * product.quantiy * 0.12) / 100.0
            totalAmount += (product.price * product.quantiy)
        }

        var formattedArticle = "0.00"
        var formattedDiscount = "0.00"
        var formattedIva = "0.00"
        var formattedTotal = "0.00"

        if (totalArticles != 0 && totalDiscount != 0.0 && totalIva != 0.0 && totalAmount != 0.0) {
            formattedArticle = String.format("%.2f", totalArticles)
            formattedDiscount = String.format("%.2f", totalDiscount)
            formattedIva = String.format("%.2f", totalIva)
            formattedTotal = String.format("%.2f", totalAmount)
        }

        billArticle?.text = formattedArticle
        billDiscount?.text = formattedDiscount
        billIva?.text = formattedIva
        billTotal?.text = formattedTotal
    }


}