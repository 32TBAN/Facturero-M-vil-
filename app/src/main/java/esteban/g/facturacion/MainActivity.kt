package esteban.g.facturacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import esteban.g.facturacion.Logic.UserLogic
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.*

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
                    if (isValid(username, password)) {
                        performLogin(username, password)
                    } else {
                        showError()
                    }
                }
            } else {
                showError()
            }
        }
    }

    private suspend fun isValid(username: String, password: String): Boolean {
        val user = UserLogic.getUserByUsernameAndPassword(username, password)
        println(user)
        return user != null
    }

    private fun performLogin(username: String, password: String) {

    }

    private fun showError() {
        println("Error al iniciar seccion")
    }


}