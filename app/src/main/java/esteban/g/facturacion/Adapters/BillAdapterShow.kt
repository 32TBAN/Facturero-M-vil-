package esteban.g.facturacion.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Product
import esteban.g.facturacion.R

class BillAdapterShow(private var items: List<Product>) :
    RecyclerView.Adapter<BillAdapterShow.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val currentItem = items[position]
        holder.bind(currentItem)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Configura los elementos del item según tu diseño
        fun bind(product: Product) {
            // Asigna los valores a los elementos de la vista según los datos del producto
        }
    }
}


