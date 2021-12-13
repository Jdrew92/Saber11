package com.misiontic.saber11.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doBeforeTextChanged
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.misiontic.saber11.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    private val tag = "MainActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        binding.btnLogin.setOnClickListener { login() }

        binding.btnRegistro.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
        binding.edtEmailLogin.doBeforeTextChanged { _, _, _, _ ->
            binding.tvLoginError.visibility = View.GONE
        }
        binding.edtPasswordLogin.doBeforeTextChanged { _, _, _, _ ->
            binding.tvLoginError.visibility = View.GONE
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser = auth.currentUser
        Log.i(tag,"currentUser onStart: $currentUser")
        if (currentUser != null) {
           toListaPreguntas()
         }
    }

    private fun login() {
        val email = binding.edtEmailLogin.text.toString().trim().lowercase()
        val password = binding.edtPasswordLogin.text.toString()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    toListaPreguntas()
                } else {
                    binding.tvLoginError.visibility = View.VISIBLE
                }
            }
    }

    private fun toListaPreguntas(){
        val intent = Intent(this, ListaPreguntasActivity::class.java)
        this.startActivity(intent)
    }

}