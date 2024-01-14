package esteban.g.facturacion.Adapters

import android.annotation.SuppressLint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.R

class ProductAdapterAdd(
    private var products: MutableList<Product>,
    private val listener: OnProductSelectedListener,
    private val updateBillFunction: () -> Unit
) :
    RecyclerView.Adapter<ProductAdapterAdd.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textProductIdAdd)
        val description: TextView = itemView.findViewById(R.id.textProductNameAdd)
        val quantity: TextView = itemView.findViewById(R.id.editTextQuantityAdd)
        val price: TextView = itemView.findViewById(R.id.textProductPriceAdd)
        val delete: Button = itemView.findViewById(R.id.buttonDeleteAdd)
        val exist: TextView = itemView.findViewById(R.id.editTextExistAdd)
        var textWatcher: TextWatcher? = null
    }

    interface OnProductSelectedListener {
        fun onProductSelectedAdd(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_productadd, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        holder.id.text = product.id.toString()
        holder.description.text = product.name
        holder.price.text = product.price.toString()
        holder.exist.text = product.stock.toString()
        product.originalStock = product.stock

        holder.delete.setOnClickListener {
            listener.onProductSelectedAdd(product)
        }

        holder.quantity.removeTextChangedListener(holder.textWatcher)
        holder.quantity.setText(product.quantiy.toString())

        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(charSequence: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(editable: Editable?) {
                val enteredQuantity = editable.toString().toIntOrNull() ?: 0
                if (enteredQuantity > product.originalStock) {
                    Toast.makeText(
                        holder.itemView.context, "No hay suficientes productos en existencia",
                        Toast.LENGTH_SHORT
                    ).show()
                    holder.quantity.setText(product.quantiy.toString())
                } else {
                    product.quantiy = enteredQuantity
                }
                updateBillFunction.invoke()
            }
        }

        // Asigna el nuevo TextWatcher al EditText
        holder.quantity.addTextChangedListener(textWatcher)
        holder.textWatcher = textWatcher

    }

    override fun getItemCount(): Int {
        return products.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(productsA: MutableList<Product>) {
        products = productsA
       notifyDataSetChanged()
    }
}