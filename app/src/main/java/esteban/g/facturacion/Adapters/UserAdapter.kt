package esteban.g.facturacion.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.User
import esteban.g.facturacion.R  // Asegúrate de cambiar esto por el paquete correcto

class UserAdapter(private var userList: List<User>) :
    RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val idTextView: TextView = itemView.findViewById(R.id.idTextView)
        private val emailTextView: TextView = itemView.findViewById(R.id.emailTextView)
        private val jobTextView: TextView = itemView.findViewById(R.id.jobTextView)

        fun bind(user: User) {
            idTextView.text = "${user.id}"
            emailTextView.text = user.email
            jobTextView.text = user.job
        }
    }

    fun updateList(listFilter: List<User>) {
        userList = listFilter.toMutableList()
        notifyDataSetChanged()
    }
}
