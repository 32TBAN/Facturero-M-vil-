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
import com.google.gson.annotations.SerializedName
import esteban.g.facturacion.Adapters.ClienteAdapter
import esteban.g.facturacion.Adapters.ProductAdapter
import esteban.g.facturacion.Adapters.ProductAdapterAdd
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.Entidades.Detail
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.Logic.BillLogic
import esteban.g.facturacion.Logic.CustomerLogic
import esteban.g.facturacion.Logic.ProductLogic
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class SaleDescription : AppCompatActivity(), ClienteAdapter.OnClienteSelectedListener,
    ProductAdapter.OnProductSelectedListener,
    ProductAdapterAdd.OnProductSelectedListener {
    private lateinit var customerAdapter: ClienteAdapter
    private var editCedula: TextView? = null
    private var editNombre: TextView? = null
    private var textViewAdrress: TextView? = null
    private var dialog: Dialog? = null
    private var dialogProduct: Dialog? = null
    private var selectedProducts: MutableList<Product> = mutableListOf()
    private var listProduct: MutableList<Product> = mutableListOf()
    private var removeListProduct: MutableList<Product> = mutableListOf()

    private lateinit var productAdapter: ProductAdapter
    private lateinit var productAdapterAdd: ProductAdapterAdd
    private var bill: Bill? = Bill(
        id = 0,
        date = "",
        idCustomer = -1,
        idUser = 1,
        subtotal = 0.0,
        total = 0.0
    )
    private var billArticle: TextView? = null
    private var billDiscount: TextView? = null
    private var billIva: TextView? = null
    private var billTotal: TextView? = null

    private fun initTextViewGlobal() {
        editCedula = findViewById(R.id.editCedula)
        editNombre = findViewById(R.id.editNombre)
        textViewAdrress = findViewById(R.id.textViewAdrressSelecd)
        billArticle = findViewById(R.id.textArticlesEdit)
        billDiscount = findViewById(R.id.textDiscountEdit)
        billIva = findViewById(R.id.textIvaEdit)
        billTotal = findViewById(R.id.textTotalEdit)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sale_description)
        initTextViewGlobal()
        val btnSearchCustomer = findViewById<Button>(R.id.btnBuscarCliente)
        val textViewFecha = findViewById<TextView>(R.id.textFechaActual)
        val btnAddBill = findViewById<Button>(R.id.btnGuardar)
        val btnSearchProduct = findViewById<Button>(R.id.btnAddProduct)
        val currentDate = Date()
        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
        val formattedDate = dateFormat.format(currentDate)
        textViewFecha.text = formattedDate
        bill?.date = textViewFecha.text as String


        bill?.idUser = intent.getIntExtra("userId",1)
        //Abrir modal cliente
        btnSearchCustomer.setOnClickListener {
            dialog = Dialog(this@SaleDescription)
            dialog!!.setContentView(R.layout.dialog_buscar_cliente)
            val editSearchCustomer = dialog!!.findViewById<EditText>(R.id.editTextBuscarCliente)

            lifecycleScope.launch {
                val recyclerViewClientes: RecyclerView =
                    dialog!!.findViewById(R.id.recyclerViewClientes)
                val listCustomer: List<Customer>? = CustomerLogic.getListCustomer()
                if (!listCustomer.isNullOrEmpty()) {
                    customerAdapter = ClienteAdapter(listCustomer, this@SaleDescription)
                    recyclerViewClientes.adapter = customerAdapter
                    recyclerViewClientes.layoutManager = LinearLayoutManager(dialog!!.context)
                    dialog!!.show()

                    editSearchCustomer.addTextChangedListener {
                        filterCustomer(it.toString(), listCustomer)
                    }
                }
            }

        }
        //modal producto
        dialogProduct = Dialog(this@SaleDescription)
        dialogProduct!!.setContentView(R.layout.dialog_search_products)
        val editSearchProducts = dialogProduct!!.findViewById<EditText>(R.id.editTextSearchProduct)

        lifecycleScope.launch {
            val recyclerViewProducts: RecyclerView =
                dialogProduct!!.findViewById(R.id.recyclerViewProducts)
            listProduct = ProductLogic.getListProduct()!!
            removeListProduct = listProduct
            if (listProduct.isNotEmpty()) {
                productAdapter = ProductAdapter(listProduct, this@SaleDescription)
                recyclerViewProducts.adapter = productAdapter
                recyclerViewProducts.layoutManager = LinearLayoutManager(dialogProduct!!.context)

                editSearchProducts.addTextChangedListener {
                    filterProduct(it.toString(), listProduct)
                }
            }
            bill?.id = BillLogic.getNumBill()
            if (bill?.id == 1) {
                Toast.makeText(
                    this@SaleDescription,
                    "Error al obtener num. orden",
                    Toast.LENGTH_SHORT
                ).show()
            }
            val billId = findViewById<TextView>(R.id.textCodFacEdit)
            billId.text = bill?.id.toString()
        }
        btnSearchProduct.setOnClickListener {
            dialogProduct!!.show()
        }

        val recyclerViewProductsAdd: RecyclerView = findViewById(R.id.recyclerViewProductsAdd)
        productAdapterAdd = ProductAdapterAdd(selectedProducts, this@SaleDescription, ::updateBill)
        recyclerViewProductsAdd.adapter = productAdapterAdd
        recyclerViewProductsAdd.layoutManager = LinearLayoutManager(this@SaleDescription)

        btnAddBill.setOnClickListener {
            if (selectedProducts.size <= 0) {
                Toast.makeText(this, "Debe escojer al menos un producto", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            for (product in selectedProducts) {
                if (product.quantiy <= 0) {
                    Toast.makeText(
                        this,
                        "No ha escogido una cantidad valida para ${product.name}",
                        Toast.LENGTH_SHORT
                    ).show()
                    return@setOnClickListener
                }
            }

            if (bill?.idCustomer == -1 ){
                Toast.makeText(
                    this,
                    "Debe escojer un cliente",
                    Toast.LENGTH_SHORT
                ).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                if (BillLogic.addBill(bill)){
                    val listDetails:MutableList<Detail> = mutableListOf()

                    for (product in selectedProducts){
                        if (bill != null){
                            listDetails.add(
                            Detail(
                                id = bill!!.id,
                                idProduct = product.id,
                                quantity = product.quantiy
                            ))
                        }
                    }

                    if(BillLogic.addDetails(listDetails)){
                        Toast.makeText(
                            this@SaleDescription,
                            "Se ha guardado la factura",
                            Toast.LENGTH_SHORT
                        ).show()
                        setResult(RESULT_OK)
                        finish()
                    }else{
                        Toast.makeText(
                            this@SaleDescription,
                            "Erro al guardar los productos",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    //TDO: CERRAR ESTA PANTALL Y ABRIR ACTULIZDO EL FRACMENT
                }else{
                    Toast.makeText(
                        this@SaleDescription,
                        "Error al agregar factura",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

        }
    }

    private fun filterProduct(query: String, listProduct: MutableList<Product>) {
        val listFilter = listProduct.filter {
            it.name.contains(query, ignoreCase = true)
        }
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
        editNombre?.setText(cliente.name + " " + cliente.lastname)
        textViewAdrress?.setText(cliente.address)
        bill?.idCustomer = cliente.id
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
            totalAmount += (product.price * product.quantiy)
            totalDiscount += totalAmount - (product.price * product.quantiy * 0.12)
            totalIva += (product.price * product.quantiy * 0.12)
        }

        bill?.total = totalDiscount - (totalDiscount * 0.88)
        bill?.subtotal = totalDiscount - (totalDiscount * 0.88)

        var formattedArticle = "0"
        val formattedDiscount = String.format("%.2f", totalDiscount)
        val formattedIva = String.format("%.2f", totalIva)
        val formattedTotal = String.format("%.2f", totalAmount)

        if (totalArticles != 0) {
            formattedArticle = totalArticles.toString()
        }

        billArticle?.text = formattedArticle
        billDiscount?.text = formattedDiscount
        billIva?.text = formattedIva
        billTotal?.text = formattedTotal
    }


}