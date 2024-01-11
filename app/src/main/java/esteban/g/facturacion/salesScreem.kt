package esteban.g.facturacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class salesScreem : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var userId: Int? = 0
    private var userJob: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_screem)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, FacturasFragment())
            .commit()

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_facturas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, FacturasFragment())
                        .commit()
                    true
                }
                R.id.menu_clientes -> {
                    true

                }
                R.id.menu_usuarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, UsuariosFragment())
                        .commit()
                    true
                }
                R.id.menu_productos -> {

                    true
                }
                else -> false
            }
        }

        userId = intent.getIntExtra("userId", 0)
        userJob = intent.getStringExtra("userJob")

        showMessangs("Welcome $userId")
    }

    private fun showMessangs(message: String) {
        Toast.makeText(this@salesScreem, message, Toast.LENGTH_SHORT).show()
    }
}