package esteban.g.facturacion

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Adapters.UserAdapter
import esteban.g.facturacion.Entidades.User
import esteban.g.facturacion.Entidades.UserSend
import esteban.g.facturacion.Logic.UserLogic
import kotlinx.coroutines.launch

class UserFragment : Fragment() {
    private var userId: Int? = null
    private var userJob: String? = null
    private lateinit var userAdapter: UserAdapter
    private var listUsers: List<User>? = null

    companion object {
        fun newInstance(userId: Int?, userJob: String?): UserFragment {
            val fragment = UserFragment()
            val args = Bundle()
            if (userId != null) {
                args.putInt("userId", userId)
                args.putString("userJob",userJob)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            userId = it.getInt("userId")
            userJob = it.getString("userJob")
        }
        val btnAddUser = view.findViewById<Button>(R.id.btnAgregarUsuario)
        val searchUser = view.findViewById<EditText>(R.id.editTextSearchUser)
        btnAddUser.visibility = if (userJob.equals("Ninguno", ignoreCase = true)) View.GONE else View.VISIBLE

        lifecycleScope.launch {
            listUsers = UserLogic.getListUser()
            if (!listUsers.isNullOrEmpty()) {
                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewUsuarios)
                userAdapter = UserAdapter(listUsers!!)
                recyclerView.adapter = userAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }

        searchUser.addTextChangedListener { query ->
            var q = query.toString()
            if (q != "") {
                val listFilter = listUsers?.filter {
                    it.id.toString().contains(q, ignoreCase = true) ||
                            it.email.contains(q, ignoreCase = true) ||
                            it.job.contains(q, ignoreCase = true)
                }
                userAdapter.updateList(listFilter!!)
            }
        }

        btnAddUser.setOnClickListener {

            val dialogBuilder = AlertDialog.Builder(requireContext())
            val inflater = layoutInflater
            val dialogView = inflater.inflate(R.layout.modal_add_user, null)
            dialogBuilder.setView(dialogView)

            val etEmail = dialogView.findViewById<EditText>(R.id.emailEditText)
            val etJob = dialogView.findViewById<Spinner>(R.id.jobSpinner)
            val etPassword = dialogView.findViewById<EditText>(R.id.passwordEditText)
            val btnCancel = dialogView.findViewById<Button>(R.id.cancelButton)
            val btnSave = dialogView.findViewById<Button>(R.id.saveButton)

            val alertDialog = dialogBuilder.create()

            btnCancel.setOnClickListener {
                alertDialog.dismiss()
            }

            btnSave.setOnClickListener {
                val email = etEmail.text.toString()
                val job = etJob.selectedItem.toString()
                val password = etPassword.text.toString()

                if (!email.isNotEmpty() && !job.isNotEmpty() && !password.isNotEmpty()) {
                    showToast("Error debe completar todos los campos")
                } else if (!isPasswordValid(password)) {
                    showToast("La contraseña no cumple con los requisitos mínimos")
                    showToast("Debe tener: Una letra masyus. una minus. un número y un caracter")
                } else {
                    val userSend = UserSend(
                        email = email,
                        password = password,
                        job = job
                    )
                    lifecycleScope.launch {
                        if (UserLogic.addUser(userSend)) {
                            showToast("Enviando....")
                            alertDialog.dismiss()
                            listUsers = UserLogic.getListUser()
                            listUsers?.let { it1 -> userAdapter.updateList(it1) }
                        } else {
                            showToast("Error al agregar....")
                        }
                    }

                }
            }
            alertDialog.show()

        }
    }
    private fun isPasswordValid(password: String): Boolean {
        val passwordRegex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[^\\da-zA-Z]).{4,}$"
        return password.matches(passwordRegex.toRegex())
    }
    private fun showToast(s: String) {
        Toast.makeText(requireContext(), s, Toast.LENGTH_SHORT).show()
    }
}