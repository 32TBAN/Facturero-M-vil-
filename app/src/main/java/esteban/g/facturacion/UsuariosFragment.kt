package esteban.g.facturacion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import esteban.g.facturacion.R  // Asegúrate de tener la referencia correcta al archivo de recursos R

class UsuariosFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_usuarios, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Puedes realizar acciones adicionales cuando la vista ha sido creada
        // Esto puede incluir la configuración de vistas, enlaces de datos, etc.
    }
}
