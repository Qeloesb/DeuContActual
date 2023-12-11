package com.example.deucont.vistas
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.deucont.databinding.FragmentAddBinding
import com.example.deucont.model.Deuda
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class AddFragment : Fragment() {

    private lateinit var  binding: FragmentAddBinding
    private  lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Deudas")
        auth = Firebase.auth
        return binding.root


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnGuardar.setOnClickListener {
            try {
                if (validateForm()){
                    val nombreDeuda = binding.etNombreDeuda.text.toString()
                    val valorDeuda = binding.etValorTotal.text.toString()
                    val valorMensual = binding.etValorMensual.text.toString()
                    val valorCuota = binding.etValorCuota.text.toString()
                    val fechaPago = binding.etFechaPago.text.toString()

                    val currentUser = auth.currentUser
                    val userId = currentUser?.uid
                    val id = userId?.let { it1 -> database.child("Deudas").child(it1).push().key }

                    val deudas = Deuda(userId, id, nombreDeuda, valorDeuda, valorMensual,valorCuota, fechaPago)
                    database.child(userId!!).child(id!!).setValue(deudas).addOnSuccessListener {

                        Snackbar.make(binding.root, "Datos enviados exitosamente", Snackbar.LENGTH_LONG)
                            .show()
                    }

                }

            }catch (e: Exception){
                Log.e("Error!", e.message.toString())
            }
        }
    }

    private fun validateForm(): Boolean {
        var isValid = true
        with(binding){
            if (etNombreDeuda.text.toString().isBlank()){
                isValid = false
                tflNombreDeuda.error="Campo Requerido"
            }else{
                tflNombreDeuda.error=null
            }
            if (etValorTotal.text.toString().isBlank()){
                isValid = false
                tflValorTotal.error="Campo Requerido"
            }else{
                tflValorTotal.error=null
            }
            if (etValorMensual.text.toString().isBlank()){
                isValid = false
                tflValorMensual.error="Campo Requerido"
            }else{
                tflValorMensual.error=null
            }
            if (etValorCuota.text.toString().isBlank()){
                isValid = false
                tflValorCuota.error="Campo Requerido"
            }else{
                tflValorCuota.error=null
            }
            if (etFechaPago.text.toString().isBlank()){
                isValid = false
                tflFechaPago.error="Campo Requerido"
            }else{
                tflFechaPago.error=null
            }
        }
        return isValid
    }
}