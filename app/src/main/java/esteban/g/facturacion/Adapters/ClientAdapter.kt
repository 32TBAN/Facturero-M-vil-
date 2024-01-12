package esteban.g.facturacion.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Customer
import esteban.g.facturacion.R

class ClienteAdapter(private var clientes: List<Customer>, private val listener: OnClienteSelectedListener) :
    RecyclerView.Adapter<ClienteAdapter.ClienteViewHolder>() {

     class ClienteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textIdCustomer: TextView = itemView.findViewById(R.id.textViewIdOrden)
        val textIdentification: TextView = itemView.findViewById(R.id.textViewIdentification)
        val textName: TextView = itemView.findViewById(R.id.textViewName)
        val textLastname: TextView = itemView.findViewById(R.id.textViewLastname)
        val textViewAdrress: TextView = itemView.findViewById(R.id.textViewAdrress)
    }

    interface OnClienteSelectedListener {
        fun onClienteSelected(cliente: Customer)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ClienteViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cliente, parent, false)
        return ClienteViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ClienteViewHolder, position: Int) {
        val customer = clientes[position]
        holder.textIdCustomer.text = customer.id.toString()
        holder.textIdentification.text = customer.identification
        holder.textName.text = customer.name
        holder.textLastname.text = customer.lastname
        holder.textViewAdrress.text = customer.address

        holder.itemView.setOnClickListener {
            listener.onClienteSelected(customer)
        }
    }

    override fun getItemCount(): Int {
        return clientes.size
    }

    fun updateList(customers: List<Customer>) {
        clientes = customers
        notifyDataSetChanged()
    }
}
