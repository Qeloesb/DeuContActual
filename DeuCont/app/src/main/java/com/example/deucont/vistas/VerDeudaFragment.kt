package com.example.deucont.vistas

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.deucont.Adapter.DeudaAdapter
import com.example.deucont.R
import com.example.deucont.databinding.FragmentVerDeudaBinding
import com.example.deucont.model.Deuda
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class VerDeudaFragment : Fragment(){

    private lateinit var  binding: FragmentVerDeudaBinding
    private lateinit var  deudasList : ArrayList<Deuda>
    private lateinit var deudaAdapter: DeudaAdapter
    private lateinit var  deudaReciclerView: RecyclerView
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentVerDeudaBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Deudas")
        auth = Firebase.auth

        deudaReciclerView = binding.rvDeudas
        deudaReciclerView.layoutManager = LinearLayoutManager(requireContext())
        deudaReciclerView.hasFixedSize()
        deudasList = arrayListOf<Deuda>()
        deudaAdapter = DeudaAdapter(deudasList)

        deudaAdapter.setOnDeleteClickListener(object : DeudaAdapter.OnDeleteClickListener{
            override fun onDeleteClick(position: Int) {
                Log.d("DeudaAdapter", "onDeleteClick posision: $position")
                val deudaId = deudasList[position].id
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Quieres Eliminar la Deuda?")
                    .setMessage("Si precionas 'Aceptar', Eliminara la Dueda")
                    .setNeutralButton("Cancelar") { dialog, which ->
                    }
                    .setPositiveButton("Aceptar") { dialog, which ->
                        Toast.makeText(requireContext(), "Cambiando la contrasena", Toast.LENGTH_SHORT).show()
                        if (deudaId != null){
                            //Eliminar la deuda de la lista
                            deudasList.removeAt(position)
                            deudaAdapter.notifyItemRemoved(position)
                            //Llamar a la funcion para eliminar la deuda de la base de datos de Firebase
                            eliminarDeuda(deudaId)
                        }else{
                            Toast.makeText(requireContext(), "ERROR al eliminar la Deuda", Toast.LENGTH_SHORT).show()
                        }
                    }
                    .show()
            }
            override fun onDeleteClick(deudaId: String) {
                eliminarDeuda(deudaId)
            }
            override fun onEditClick(deuda: Deuda) {
                Log.d("VerDeudaFragment", "onEditClick llamado para deuda con ID: ${deuda.id}")
                showEditDialog(deuda)
            }
        })


        getDeudas()
        return binding.root



    }


    private fun showEditDialog(deuda: Deuda) {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.dialog_edit, null)

        // Inicializar vistas del diálogo y establecer valores iniciales
        val newNombre = dialogView.findViewById<EditText>(R.id.etNewNombreDeuda)
        val newValorDeuda = dialogView.findViewById<EditText>(R.id.etNewValorTotal)
        val newValorMensual = dialogView.findViewById<EditText>(R.id.etNewValorMensual)
        val newCantidadCuotas = dialogView.findViewById<EditText>(R.id.etNewCantidadCuota)
        val newFechaPago = dialogView.findViewById<EditText>(R.id.etNewFechaPago)


        newNombre.setText(deuda.nombreDeuda)
        newValorDeuda.setText(deuda.valorDeuda)
        newValorMensual.setText(deuda.valorMensual)
        newCantidadCuotas.setText(deuda.cantidadCuotas)
        newFechaPago.setText(deuda.fechaPago)


        // Configurar botones del diálogo
        builder.setPositiveButton("Guardar") { dialog, which ->
            // Obtener los nuevos valores del diálogo
            val nuevoNombre = newNombre.text.toString()
            val nuevoValorDeuda = newValorDeuda.text.toString()
            val nuevoValorMensual = newValorMensual.text.toString()
            val nuevoCantidadCuotas = newCantidadCuotas.text.toString()
            val nuevoFechaPago = newFechaPago.text.toString()


            // Actualizar la Deuda en la base de datos o donde sea necesario
            actualizarDeuda(deuda.id, nuevoNombre, nuevoValorDeuda, nuevoValorMensual,nuevoCantidadCuotas,nuevoFechaPago /*otros valores*/)
        }

        builder.setNegativeButton("Cancelar") { dialog, which ->
            dialog.cancel()
        }

        builder.setView(dialogView)
        builder.show()
    }
    private fun actualizarDeuda(
        deudaId: String?,
        nuevoNombre: String,
        nuevoValorDeuda: String,
        nuevoValorMensual: String,
        nuevoCantidadCuotas: String,
        nuevoFechaPago: String
    ) {
        val currentUser = auth.currentUser
        val userId = currentUser?.uid
        if (deudaId != null) {
            if (userId != null && deudaId.isNotBlank()) {
                val referenciaDeuda = FirebaseDatabase.getInstance().getReference("Deudas").child(userId)
                    .child(deudaId)

                // Crea un mapa para almacenar los campos que deseas actualizar
                val actualizacionDeudaMap = mutableMapOf<String, Any>()
                actualizacionDeudaMap["nombreDeuda"] = nuevoNombre
                actualizacionDeudaMap["valorDeuda"] = nuevoValorDeuda
                actualizacionDeudaMap["valorMensual"] = nuevoValorMensual
                actualizacionDeudaMap["cantidadCuotas"] = nuevoCantidadCuotas
                actualizacionDeudaMap["fechaPago"] = nuevoFechaPago

                // Utiliza el método updateChildren para realizar la actualización en la base de datos
                referenciaDeuda.updateChildren(actualizacionDeudaMap)
                    .addOnSuccessListener {
                        Toast.makeText(requireContext(), "La Deuda se actualizo Correctamente", Toast.LENGTH_SHORT).show()

                    }
                    .addOnFailureListener { e ->
                        Log.e(
                            "ActualizarDeuda",
                            "Error al actualizar deuda en Firebase: ${e.message}"
                        )
                    }
            } else {
                Log.e("ActualizarDeuda", "userId o deudaId es nulo o vacío")
            }
        }
    }

    private  fun eliminarDeuda(deudaId: String){
        Log.d("EliminarDeuda", "eliminardeuda")
        val currentUser = auth.currentUser
        val  userId = currentUser?.uid
        if (userId != null && deudaId.isNotBlank()){
            database = FirebaseDatabase.getInstance().getReference("Deudas").child(userId).child(deudaId)

            //Eliminar la deuda de la Base de Datos
            database.removeValue()
                .addOnSuccessListener {
                    Log.d("EliminarDeudaFirebase", "Deuda eliminada correctamente")
                }
                .addOnFailureListener { e ->
                    Log.e("EliminarDeudaFirebase", "Error al eliminar deuda de Firebase: ${e.message}")
                }
        }else{
            Log.e("EliminarDeudaFirebase", "userId o deudaId es nulo o vacío")
        }
    }


    private fun getDeudas() {

        val currentUser = auth.currentUser
        var userId = currentUser?.uid
        if(userId != null){
            
            database = FirebaseDatabase.getInstance().getReference("Deudas").child(userId)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for ( deudasSnapshot in snapshot.children){
                            val  deuda = deudasSnapshot.getValue(Deuda::class.java)
                            deudasList.add(deuda!!)
                        }

                        deudaReciclerView.adapter = deudaAdapter
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e("DatabaseError", "Operation cancelled: ${error.message}")
                }
            })
        }
    }

}