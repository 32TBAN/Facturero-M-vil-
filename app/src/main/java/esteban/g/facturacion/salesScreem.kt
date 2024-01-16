package esteban.g.facturacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

class salesScreem : AppCompatActivity() {
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navigationView: NavigationView
    private var userId: Int? = 1
    private var userJob: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_screem)

        drawerLayout = findViewById(R.id.drawerLayout)
        navigationView = findViewById(R.id.navigationView)

        userId = intent.getIntExtra("userId", 1)
        userJob = intent.getStringExtra("userJob")

        val toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val facturasFragment = FacturasFragment.newInstance(userId,userJob)
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, facturasFragment)
            .commit()
        val customerFragment = CustomerFragment.newInstance(userId,userJob)
        val userFragment = UserFragment.newInstance(userId,userJob)
        val productFragment = ProductFragment.newInstance(userId,userJob)

        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.menu_facturas -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, facturasFragment)
                        .commit()
                    true
                }
                R.id.menu_clientes -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, customerFragment)
                        .commit()
                    true
                }
                R.id.menu_usuarios -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, userFragment)
                        .commit()
                    true
                }
                R.id.menu_productos -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.container, productFragment)
                        .commit()
                    true
                }
                else -> false
            }
        }


        showMessangs("Welcome $userId")
    }

    private fun showMessangs(message: String) {
        Toast.makeText(this@salesScreem, message, Toast.LENGTH_SHORT).show()
    }
}