package esteban.g.facturacion

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Bill

class BillAdapter(private val bills: List<Bill>) : RecyclerView.Adapter<BillAdapter.BillViewHolder>() {

    class BillViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewFactura: TextView = itemView.findViewById(R.id.textViewIdOrden)
        val textViewFecha: TextView = itemView.findViewById(R.id.textViewFecha)
        val buttonDelete: Button = itemView.findViewById(R.id.buttonDelete)
        val btnShow: TextView = itemView.findViewById(R.id.buttonShow)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BillViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_factura, parent, false)
        return BillViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BillViewHolder, position: Int) {
        val currentBill = bills[position]
        holder.textViewFactura.text = currentBill.id.toString()
        holder.textViewFecha.text = currentBill.date.toString()
    }

    override fun getItemCount() = bills.size
}
