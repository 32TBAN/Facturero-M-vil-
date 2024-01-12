package esteban.g.facturacion
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import esteban.g.facturacion.Entidades.Bill
import esteban.g.facturacion.Logic.BillLogic
import kotlinx.coroutines.launch

class FacturasFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_facturas, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonAddBill = view.findViewById<Button>(R.id.btnAgregarFactura)

        buttonAddBill.setOnClickListener{
            val intent = Intent(requireContext(), SaleDescription::class.java)
            startActivity(intent)
        }

        lifecycleScope.launch {
            val listBills: List<Bill>? = BillLogic.listaFacturas();

            if (!listBills.isNullOrEmpty()) {
                val recyclerView: RecyclerView = view.findViewById(R.id.recyclerViewFacturas)
                val billAdapter = BillAdapter(listBills)
                recyclerView.adapter = billAdapter
                recyclerView.layoutManager = LinearLayoutManager(requireContext())
            }
        }


    }


}