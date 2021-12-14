package com.misiontic.saber11.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.misiontic.saber11.R
import com.misiontic.saber11.databinding.ActivityNewPreguntaBinding
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.enums.Categoria
import com.misiontic.saber11.utils.Database
import java.util.*
import kotlin.collections.ArrayList

class NewPreguntaActivity : AppCompatActivity() {

    private var categorias: ArrayList<Any> = ArrayList()
    private lateinit var newPreguntaBinding: ActivityNewPreguntaBinding

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newPreguntaBinding = ActivityNewPreguntaBinding.inflate(layoutInflater)
        setContentView(newPreguntaBinding.root)
        initSpinner()

        auth = Firebase.auth
        Firebase.initialize(this)

        newPreguntaBinding.tvErrorRadGroup.visibility = View.GONE


        newPreguntaBinding.btnGuardarPregunta.setOnClickListener { guardarPregunta() }

        newPreguntaBinding.spCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                newPreguntaBinding.spCategoria.errorText = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                newPreguntaBinding.spCategoria.errorText = getString(R.string.categoria_error_msg)
            }

        }

    }

    private fun guardarPregunta() {
        if(allInfoIsFilled()) {
            val optCorrecta = when(newPreguntaBinding.rdGroupRespuestas.checkedRadioButtonId){
                R.id.radBtnA -> getString(R.string.option_a)
                R.id.radBtnB -> getString(R.string.option_b)
                R.id.radBtnC -> getString(R.string.option_c)
                else -> getString(R.string.option_d)
            }
            val correcta = when(newPreguntaBinding.rdGroupRespuestas.checkedRadioButtonId){
                R.id.radBtnA -> newPreguntaBinding.edtRespuesta1.text.toString()
                R.id.radBtnB -> newPreguntaBinding.edtRespuesta2.text.toString()
                R.id.radBtnC -> newPreguntaBinding.edtRespuesta3.text.toString()
                else -> newPreguntaBinding.edtRespuesta4.text.toString()
            }
            val pregunta = Pregunta(
                UUID.randomUUID().toString(),
                newPreguntaBinding.edtDescripcion.text.toString(),
                newPreguntaBinding.edtRespuesta1.text.toString(),
                newPreguntaBinding.edtRespuesta2.text.toString(),
                newPreguntaBinding.edtRespuesta3.text.toString(),
                newPreguntaBinding.edtRespuesta4.text.toString(),
                correcta,
                optCorrecta,
                null,
                newPreguntaBinding.spCategoria.selectedItem.toString()
            )
            Database.getPreguntasReference().child(pregunta.id!!).setValue(pregunta)
            Toast.makeText(this, getString(R.string.registro_exitoso), Toast.LENGTH_LONG).show()
            val intent = Intent(this,  ListaPreguntasActivity::class.java)
            finish()
            startActivity(intent)
        }
    }

    private fun initSpinner() {
        for(categoria in Categoria.values()){
            categorias.add(categoria.value)
        }
        newPreguntaBinding.spCategoria.item = categorias
    }

    private fun allInfoIsFilled(): Boolean {
        return when {
            newPreguntaBinding.edtDescripcion.text.isNullOrEmpty() -> {
                newPreguntaBinding.edtDescripcion.requestFocus()
                newPreguntaBinding.edtDescripcion.error = getString(R.string.blank_error_msg)
                false
            }
            newPreguntaBinding.edtRespuesta1.text.isNullOrEmpty() -> {
                newPreguntaBinding.edtRespuesta1.requestFocus()
                newPreguntaBinding.edtRespuesta1.error = getString(R.string.blank_error_msg)
                false
            }
            newPreguntaBinding.edtRespuesta2.text.isNullOrEmpty() -> {
                newPreguntaBinding.edtRespuesta2.requestFocus()
                newPreguntaBinding.edtRespuesta2.error = getString(R.string.blank_error_msg)
                false
            }
            newPreguntaBinding.edtRespuesta3.text.isNullOrEmpty() -> {
                newPreguntaBinding.edtRespuesta3.requestFocus()
                newPreguntaBinding.edtRespuesta3.error = getString(R.string.blank_error_msg)
                false
            }
            newPreguntaBinding.edtRespuesta4.text.isNullOrEmpty() -> {
                newPreguntaBinding.edtRespuesta4.requestFocus()
                newPreguntaBinding.edtRespuesta4.error = getString(R.string.blank_error_msg)
                false
            }
            newPreguntaBinding.spCategoria.selectedItem.toString().isEmpty() -> {
                newPreguntaBinding.spCategoria.requestFocus()
                newPreguntaBinding.spCategoria.errorText = getString(R.string.rol_error_msg)
                false
            }
            else -> {
                if(!(newPreguntaBinding.radBtnA.isChecked ||
                            newPreguntaBinding.radBtnB.isChecked ||
                            newPreguntaBinding.radBtnC.isChecked ||
                            newPreguntaBinding.radBtnD.isChecked)){
                    newPreguntaBinding.tvErrorRadGroup.visibility = View.VISIBLE
                    false
                } else {
                    true
                }
            }
        }
    }
}