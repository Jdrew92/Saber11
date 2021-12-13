package com.misiontic.saber11.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.misiontic.saber11.R
import com.misiontic.saber11.adapters.CuestionarioAdapter
import com.misiontic.saber11.databinding.ActivityCuestionarioBinding
import com.misiontic.saber11.entities.Test
import com.misiontic.saber11.entities.Usuario
import com.misiontic.saber11.utils.Database

class CuestionarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuestionarioBinding
    private lateinit var adapter: CuestionarioAdapter
    private lateinit var llm: LinearLayoutManager

    private lateinit var auth: FirebaseAuth
    private lateinit var test: Test
    private lateinit var usuario: Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuestionarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        Firebase.initialize(this)

        usuario = intent.extras?.get("usuario") as Usuario
        test = intent.extras?.get("test") as Test
        if (test.calificacion == "") {
            test.calificacion = 0
        } else {
            binding.btnResolver.text = getString(R.string.nuevo_intento)
        }
        adapter = CuestionarioAdapter(test, this)
        llm = LinearLayoutManager(this)
        binding.lstCuestionario.layoutManager = llm
        binding.lstCuestionario.adapter = adapter
        binding.lstCuestionario.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )

        binding.btnResolver.setOnClickListener {
            guardarTest()
        }
    }

    private fun guardarTest() {
        val newTest = adapter.obtenerTest()
        var puntaje = 0
        for(pregunta in newTest.preguntas!!){
            if(pregunta.marcada == pregunta.opcionCorrecta){
                puntaje++
            }
        }
        newTest.calificacion = puntaje
        Database.getTestsReference().child(auth.currentUser!!.uid).child(newTest.id!!)
            .setValue(newTest)
        dialogMessage(newTest)
    }

    private fun dialogMessage(test: Test){
        val alert: AlertDialog = this.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setPositiveButton("Aceptar") { _, _ ->
                    val intent = Intent(this@CuestionarioActivity, ListaTestsActivity::class.java)
                    intent.putExtra("usuario", usuario)
                    finish()
                    startActivity(intent)
                }
            }
            val message = if (test.calificacion.toString().toInt() >= 3) {
                getString(
                    R.string.success_test,
                    getString(R.string.success),
                    test.calificacion.toString()
                )
            } else {
                getString(
                    R.string.failure_test,
                    getString(R.string.failure),
                    test.calificacion.toString()
                )
            }
            builder.setMessage(message).setTitle(getString(R.string.resultados))
            builder.create()
        }
        alert.show()
    }
}