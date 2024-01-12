package esteban.g.facturacion.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.R

class ProductAdapter(private var products: List<Product>, private val listener: OnProductSelectedListener) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val id: TextView = itemView.findViewById(R.id.textViewIdSearchProduct)
        val description: TextView = itemView.findViewById(R.id.textViewDescriptionSearchProduct)
        val exist: TextView = itemView.findViewById(R.id.textViewExistSearchProduct)
        val price: TextView = itemView.findViewById(R.id.textPriceSearchProduct)
    }

    interface OnProductSelectedListener {
        fun onProductSelected(product: Product)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val customer = products[position]
        holder.id.text = customer.id.toString()
        holder.description.text = customer.name
        holder.exist.text = customer.stock.toString()
        holder.price.text = customer.price.toString()

        holder.itemView.setOnClickListener {
            listener.onProductSelected(customer)
        }
    }

    override fun getItemCount(): Int {
        return products.size
    }

    fun updateList(productsA: List<Product>) {
        products = productsA
        notifyDataSetChanged()
    }


}
