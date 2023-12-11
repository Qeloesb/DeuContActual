package com.example.deucont.vistas

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import com.example.deucont.MainActivity
import com.example.deucont.R
import com.example.deucont.databinding.ActivityMainBinding
import com.example.deucont.databinding.ActivityRegUserBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class RegUserActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityRegUserBinding
    private  lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnReg.setOnClickListener{
            val correo : String = binding.etEmailReg.text.toString()
            val pass1 : String = binding.etPass1.text.toString()
            val pass2 : String = binding.etPass2.text.toString()

            if (correo.isEmpty()){
                binding.etEmailReg.error = "Ingresa un correo"
                return@setOnClickListener
            }
            if (pass1.isEmpty()){
                binding.etPass1.error = "Ingrese una contrasena"
            }
            if (pass2.isEmpty()){
                binding.etPass2.error = "Ingrese confirmacion de contrasena"
            }
            if (pass1 == pass2){
                regUser(correo, pass1)
            }
        }
    }

    private fun regUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(it.isSuccessful){
                    Toast.makeText(this, "El usuario se ha creado correctamente", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }else{
                    Toast.makeText(this, "" +
                            "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
    }
}