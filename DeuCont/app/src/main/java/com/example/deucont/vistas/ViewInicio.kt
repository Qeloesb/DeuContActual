package com.example.deucont.vistas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.deucont.R
import com.example.deucont.databinding.ActivityViewInicioBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference

class ViewInicio : AppCompatActivity() {

    private lateinit var  binding: ActivityViewInicioBinding
    private  lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)
            binding = ActivityViewInicioBinding.inflate(layoutInflater)
            setContentView(binding.root)
            auth = Firebase.auth

            supportFragmentManager.beginTransaction().replace(R.id.frameLayout, HomeFragment()).commit()

            val bottomNavigationView = findViewById<BottomNavigationView>(R.id.botton_nav)
            bottomNavigationView.setOnItemSelectedListener { item ->

                when(item.itemId){
                    R.id.nav_home ->{
                        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,HomeFragment()).commit()
                        true
                    }
                    R.id.nav_view_deuda ->{
                        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,VerDeudaFragment()).commit()
                        true
                    }
                    R.id.nav_add ->{
                        supportFragmentManager.beginTransaction().replace(R.id.frameLayout,AddFragment()).commit()
                        true
                    }
                    else ->false
                }
            }
        }catch (e: Exception){
            Log.e("Error!", e.message.toString())
        }

    }
}