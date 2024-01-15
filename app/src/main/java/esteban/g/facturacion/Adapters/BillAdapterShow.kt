package esteban.g.facturacion.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.DetailGet
import esteban.g.facturacion.R

class BillAdapterShow(private var items: List<DetailGet>) :
    RecyclerView.Adapter<BillAdapterShow.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura_header, parent, false)
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
        private val nombreTextView: TextView = itemView.findViewById(R.id.textViewNombreShow)
        private val cantidadTextView: TextView = itemView.findViewById(R.id.textViewCantidadShow)
        private val precioTextView: TextView = itemView.findViewById(R.id.textViewPrecioShow)
        private val subtotalTextView: TextView = itemView.findViewById(R.id.textViewSubtotalShow)
        private val totalTextView: TextView = itemView.findViewById(R.id.textViewTotalShow)

        fun bind(detail: DetailGet) {
            nombreTextView.text = detail.name
            cantidadTextView.text = detail.quantity.toString()
            precioTextView.text = detail.price.toString()
            val formatPrice = String.format("%.2f", detail.price*detail.quantity* 0.88)
            subtotalTextView.text = formatPrice
            totalTextView.text = (detail.price*detail.quantity).toString()
        }
    }
}
