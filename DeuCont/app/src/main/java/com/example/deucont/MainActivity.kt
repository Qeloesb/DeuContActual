package com.example.deucont

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import com.example.deucont.databinding.ActivityMainBinding
import com.example.deucont.vistas.RegUserActivity
import com.example.deucont.vistas.ViewInicio
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthProvider
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Deudas")
        auth = Firebase.auth

        binding.btniniciarsesion.setOnClickListener {
            try {
                val correo = binding.etEmail.text.toString()
                val contrasena = binding.etPassword.text.toString()

                if (correo.isEmpty()){
                    binding.etEmail.error = "Ingresa un correo"
                    return@setOnClickListener
                }
                if (contrasena.isEmpty()){
                    binding.etPassword.error = "Ingresar una contraseña"
                    return@setOnClickListener
                }
                signIn(correo, contrasena)

            }catch (e: Exception){
                Log.e("Error!", e.message.toString())
            }

        }
        binding.btnRegUser.setOnClickListener {
            val intent = Intent(this, RegUserActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "Se inicio la sesion correctamente", Toast.LENGTH_LONG).show()
                    val intent = Intent (this, ViewInicio::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "ERROR, Correo o Contraseña Incorrecto", Toast.LENGTH_LONG).show()
                }
            }
    }
}


