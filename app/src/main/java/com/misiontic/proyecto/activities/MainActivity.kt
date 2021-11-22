package com.misiontic.proyecto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.core.widget.doBeforeTextChanged
import com.misiontic.proyecto.R
import com.misiontic.proyecto.database.Saber11Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private lateinit var edtEmailLogin: EditText
    private lateinit var edtPasswordLogin: EditText
    private lateinit var tvLoginError: TextView
    private lateinit var btnRegistrar: Button
    private lateinit var btnLogin: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        edtEmailLogin = findViewById(R.id.edtEmailLogin)
        edtPasswordLogin = findViewById(R.id.edtPasswordLogin)
        tvLoginError = findViewById(R.id.tvLoginError)
        btnLogin = findViewById(R.id.btnLogin)
        btnRegistrar = findViewById(R.id.btnRegistro)

        btnLogin.setOnClickListener { login() }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
        edtEmailLogin.doBeforeTextChanged { text, start, count, after -> tvLoginError.visibility = View.GONE }
        edtPasswordLogin.doBeforeTextChanged { text, start, count, after -> tvLoginError.visibility = View.GONE }
    }

    private fun login(){
        val db = Saber11Database.getDatabase(this)
        val usuarioDao = db.usuarioDao()
        val email = edtEmailLogin.text.toString().trim().lowercase()
        val password = edtPasswordLogin.text.toString()
        runBlocking {
            launch {
                val usuario = usuarioDao.getUsuarioByEmailAndPassword(email, password)
                if(usuario.isNotEmpty()){
                    val intent = Intent(this@MainActivity, ListaPreguntasActivity::class.java)
                    intent.putExtra("id", usuario.first().id)
                    intent.putExtra("rol", usuario.first().rol)
                    //println("${usuario.first().id}, ${usuario.first().rol}")
                    finish()
                    startActivity(intent)
                } else {
                    tvLoginError.visibility = View.VISIBLE
                }
            }
        }
    }
}