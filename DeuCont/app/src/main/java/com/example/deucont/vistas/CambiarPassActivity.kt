package com.example.deucont.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.deucont.MainActivity
import com.example.deucont.R
import com.example.deucont.databinding.ActivityCambiarPassBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.Firebase
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class CambiarPassActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCambiarPassBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCambiarPassBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = Firebase.auth

        binding.btnCambiarPass.setOnClickListener{
            val user = Firebase.auth.currentUser!!

            val passOld = binding.etOldPass.text.toString()
            val passNew = binding.etNewPass1.text.toString()
            val passNew2 = binding.etNewPass2.text.toString()

            if (passOld.isEmpty()){
                binding.etOldPass.error = "Ingresa una contrasena"
                return@setOnClickListener
            }
            if (passNew.isEmpty()){
                binding.etNewPass1.error = "Ingrese su nueva contrasena"
                return@setOnClickListener
            }
            if (passNew2.isEmpty()){
                binding.etNewPass2.error = "Ingrese confirmacion de la nueva contrasena"
                return@setOnClickListener
            }

            val credential = EmailAuthProvider
                .getCredential(user!!.email.toString(), passOld)

            user. reauthenticate(credential)
                .addOnCompleteListener{
                    if (it.isSuccessful){
                        MaterialAlertDialogBuilder(this)
                            .setTitle("Quieres Cambiar la Contrasena?")
                            .setMessage("Si precionas 'Aceptar', Cambiara la contrasena actual")
                            .setNeutralButton("Cancelar") { dialog, which ->
                            }
                            .setPositiveButton("Aceptar") { dialog, which ->
                                Toast.makeText(this, "Cambiando la contrasena", Toast.LENGTH_SHORT).show()
                                if (passNew == passNew2){
                                    user!!.updatePassword(passNew)
                                        .addOnCompleteListener{task ->
                                            if (task.isSuccessful){
                                                Toast.makeText(this, "Contrasena Actualizada", Toast.LENGTH_SHORT).show()
                                                val intent = Intent(this, HomeFragment::class.java)
                                                startActivity(intent)
                                            }
                                        }
                                }else{Toast.makeText(this, "Contrasenas no coinciden", Toast.LENGTH_SHORT).show()

                                }

                            }
                            .show()
                    }else{
                        Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                }


        }
    }
}