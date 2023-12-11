package com.example.deucont.vistas
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.deucont.MainActivity
import com.example.deucont.R
import com.example.deucont.databinding.FragmentHomeBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth


class HomeFragment : Fragment() {
    private lateinit var  binding: FragmentHomeBinding
    private  lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {


        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        auth = Firebase.auth


        binding.btnLogout.setOnClickListener{
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Quieres Salir")
                .setMessage("Si precionas 'Cerrar Sesión', Cerraras la sesión en DeuCont")
                .setNeutralButton("Cancelar") { dialog, which ->

                }
                .setPositiveButton("Cerrar Sesión") { dialog, which ->

                    auth.signOut()
                    val intent = Intent (requireContext(), MainActivity::class.java)
                    startActivity(intent)
                    Toast.makeText(requireContext(), "Se Cerro Sesión", Toast.LENGTH_LONG).show()
                }
                .show()
        }

        binding.btnActPass.setOnClickListener {
            val intent = Intent (requireContext(), CambiarPassActivity::class.java)
            startActivity(intent)
        }
        return binding.root
    }



    companion object {
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}