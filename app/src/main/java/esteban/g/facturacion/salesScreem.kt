package esteban.g.facturacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.widget.EditText
import android.widget.Toast

class salesScreem : AppCompatActivity() {
    private var userId: Int? = 0
    private var userJob: String? = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sales_screem)

         userId = intent.getIntExtra("userId", 0)
         userJob = intent.getStringExtra("userJob")

        showMessangs("Welcome $userId")
    }

    private fun showMessangs(message: String) {
        Toast.makeText(this@salesScreem ,message, Toast.LENGTH_SHORT).show()
    }
}