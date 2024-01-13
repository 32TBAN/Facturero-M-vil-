package esteban.g.facturacion.Adapters

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

class ProductAdapterAdd(private var products: MutableList<Product>, private val listener: OnProductSelectedListener) :
    RecyclerView.Adapter<ProductAdapterAdd.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textProductIdAdd)
        val description: TextView = itemView.findViewById(R.id.textProductNameAdd)
        val qunatity: TextView = itemView.findViewById(R.id.editTextQuantityAdd)
        val price: TextView = itemView.findViewById(R.id.textProductPriceAdd)
        val delete: Button = itemView.findViewById(R.id.buttonDeleteAdd)
        val exist: TextView = itemView.findViewById(R.id.editTextExistAdd)
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
        holder.qunatity.text = "1"
        holder.price.text = product.price.toString()
        holder.exist.text = product.stock.toString()
        holder.delete.setOnClickListener {
            listener.onProductSelectedAdd(product)
        }

        holder.qunatity.addTextChangedListener{
                text ->
            val enteredQuantity = text.toString().toIntOrNull() ?: 0
            if (enteredQuantity > product.stock || enteredQuantity < 0) {
                // Si la cantidad ingresada es mayor que el stock, muestra un mensaje
                Toast.makeText(holder.itemView.context, "Error: Cantidad invalida", Toast.LENGTH_SHORT).show()
            } else {
                // Si la cantidad es válida, actualizar la cantidad en el objeto Product
                product.quantiy = enteredQuantity
                holder.qunatity.setText(product.quantiy.toString())
            }
        }

    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateList(productsA: MutableList<Product>) {
        products = productsA
        notifyDataSetChanged()
    }
}