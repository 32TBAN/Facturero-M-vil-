package esteban.g.facturacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import esteban.g.facturacion.Entidades.User
import esteban.g.facturacion.Logic.UserLogic
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private var usernameEditText: EditText? = null
    private var passwordEditText: EditText? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        val loginButton = findViewById<Button>(R.id.loginButton)

        loginButton.setOnClickListener {
            val username = usernameEditText?.text?.toString() ?: ""
            val password = passwordEditText?.text?.toString() ?: ""

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val user =  UserLogic.getUserByUsernameAndPassword(username, password)
                    if (user != null) {
                        Toast.makeText(this@MainActivity,"Correcto Ingresando",Toast.LENGTH_SHORT).show()
                        performLogin(user)
                    } else {
                        showError()
                    }
                }
            } else {
                showError()
            }
        }
    }

    private fun performLogin(user: User) {
        val intent = Intent(this, salesScreem::class.java).apply {
            putExtra("userId", user.id)
            putExtra("userJob", user.job)
        }
        startActivity(intent)
    }

    private fun showError() {
        Toast.makeText(this@MainActivity,"Error en las credenciales",Toast.LENGTH_SHORT).show()
    }


}