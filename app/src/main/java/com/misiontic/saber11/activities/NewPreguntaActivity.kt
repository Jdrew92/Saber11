package com.misiontic.saber11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.misiontic.saber11.enums.Categoria
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.R
import com.misiontic.saber11.database.Saber11Database
import com.misiontic.saber11.databinding.ActivityNewPreguntaBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.newFixedThreadPoolContext
import kotlinx.coroutines.runBlocking

class NewPreguntaActivity : AppCompatActivity() {

    private var categorias: ArrayList<Any> = ArrayList()
    private lateinit var newPreguntaBinding: ActivityNewPreguntaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        newPreguntaBinding = ActivityNewPreguntaBinding.inflate(layoutInflater)
        setContentView(newPreguntaBinding.root)
        initSpinner()

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
            val db = Saber11Database.getDatabase(this)
            val preguntaDao = db.preguntaDao()
            val correcta = when(newPreguntaBinding.rdGroupRespuestas.checkedRadioButtonId){
                R.id.radBtnA -> getString(R.string.option_a)
                R.id.radBtnB -> getString(R.string.option_b)
                R.id.radBtnC -> getString(R.string.option_c)
                else -> getString(R.string.option_d)
            }
            val pregunta = Pregunta(0,
                newPreguntaBinding.edtDescripcion.text.toString(),
                newPreguntaBinding.edtRespuesta1.text.toString(),
                newPreguntaBinding.edtRespuesta2.text.toString(),
                newPreguntaBinding.edtRespuesta3.text.toString(),
                newPreguntaBinding.edtRespuesta4.text.toString(),
                correcta,
                newPreguntaBinding.spCategoria.selectedItem.toString()
            )
            runBlocking {
                launch {
                    val result = preguntaDao.insert(pregunta)
                    if(result != -1L){
                        Toast.makeText(this@NewPreguntaActivity, "Se ha registrado con éxito!", Toast.LENGTH_LONG).show()
                        val intent = Intent(this@NewPreguntaActivity, ListaPreguntasActivity::class.java)
                        intent.putExtra("rol", getIntent().getStringExtra("rol"))
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }

    private fun initSpinner() {
        categorias.add(Categoria.LECTURA_CRITICA.value)
        categorias.add(Categoria.MATEMATICAS.value)
        categorias.add(Categoria.SOCIALES_CIUDADANAS.value)
        categorias.add(Categoria.CIENCIAS_NATURALES.value)
        categorias.add(Categoria.INGLES.value)
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
            newPreguntaBinding.spCategoria.selectedItem.toString().isNullOrEmpty() -> {
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