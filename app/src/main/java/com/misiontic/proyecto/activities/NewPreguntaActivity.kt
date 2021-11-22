package com.misiontic.proyecto.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.misiontic.proyecto.Entities.Categoria
import com.misiontic.proyecto.Entities.Pregunta
import com.misiontic.proyecto.R
import com.misiontic.proyecto.database.Saber11Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class NewPreguntaActivity : AppCompatActivity() {

    private var categorias: ArrayList<String> = ArrayList()
    private lateinit var edtDescripcion: EditText
    private lateinit var edtRespuesta1: EditText
    private lateinit var edtRespuesta2: EditText
    private lateinit var edtRespuesta3: EditText
    private lateinit var edtRespuesta4: EditText
    private lateinit var radBtnA: RadioButton
    private lateinit var radBtnB: RadioButton
    private lateinit var radBtnC: RadioButton
    private lateinit var radBtnD: RadioButton
    private lateinit var tvErrorRadGroup: TextView
    private lateinit var spCategoria: SmartMaterialSpinner<String>
    private lateinit var btnGuardarPregunta: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_pregunta)
        initSpinner()
        edtDescripcion = findViewById(R.id.edtDescripcion)
        edtRespuesta1 = findViewById(R.id.edtRespuesta1)
        edtRespuesta2 = findViewById(R.id.edtRespuesta2)
        edtRespuesta3 = findViewById(R.id.edtRespuesta3)
        edtRespuesta4 = findViewById(R.id.edtRespuesta4)
        radBtnA = findViewById(R.id.radBtnA)
        radBtnB = findViewById(R.id.radBtnB)
        radBtnC = findViewById(R.id.radBtnC)
        radBtnD = findViewById(R.id.radBtnD)
        tvErrorRadGroup = findViewById(R.id.tvErrorRadGroup)
        spCategoria = findViewById(R.id.spCategoria)
        btnGuardarPregunta = findViewById(R.id.btnGuardarPregunta)

        tvErrorRadGroup.visibility = View.GONE

        btnGuardarPregunta = findViewById(R.id.btnGuardarPregunta)
        btnGuardarPregunta.setOnClickListener { guardarPregunta() }

        spCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                spCategoria.errorText = null
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                spCategoria.errorText = getString(R.string.categoria_error_msg)
            }

        }

    }

    private fun guardarPregunta() {
        if(allInfoIsFilled()) {
            val db = Saber11Database.getDatabase(this)
            val preguntaDao = db.preguntaDao()
            val correcta = if(radBtnA.isChecked) {
                getString(R.string.option_a)
            }
            else if(radBtnB.isChecked) {
                getString(R.string.option_b)
            } else if (radBtnC.isChecked){
                getString(R.string.option_c)
            } else {
                getString(R.string.option_d)
            }
            val pregunta = Pregunta(0,
                edtDescripcion.text.toString(),
                edtRespuesta1.text.toString(),
                edtRespuesta2.text.toString(),
                edtRespuesta3.text.toString(),
                edtRespuesta4.text.toString(),
                correcta,
                spCategoria.selectedItem
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
        spCategoria = findViewById(R.id.spCategoria)
        categorias.add(Categoria.LECTURA_CRITICA.value)
        categorias.add(Categoria.MATEMATICAS.value)
        categorias.add(Categoria.SOCIALES_CIUDADANAS.value)
        categorias.add(Categoria.CIENCIAS_NATURALES.value)
        categorias.add(Categoria.INGLES.value)
        spCategoria.item = categorias
    }

    private fun allInfoIsFilled(): Boolean {
        return when {
            edtDescripcion.text.isNullOrEmpty() -> {
                edtDescripcion.requestFocus()
                edtDescripcion.error = getString(R.string.blank_error_msg)
                false
            }
            edtRespuesta1.text.isNullOrEmpty() -> {
                edtRespuesta1.requestFocus()
                edtRespuesta1.error = getString(R.string.blank_error_msg)
                false
            }
            edtRespuesta2.text.isNullOrEmpty() -> {
                edtRespuesta2.requestFocus()
                edtRespuesta2.error = getString(R.string.blank_error_msg)
                false
            }
            edtRespuesta3.text.isNullOrEmpty() -> {
                edtRespuesta3.requestFocus()
                edtRespuesta3.error = getString(R.string.blank_error_msg)
                false
            }
            edtRespuesta4.text.isNullOrEmpty() -> {
                edtRespuesta4.requestFocus()
                edtRespuesta4.error = getString(R.string.blank_error_msg)
                false
            }
            spCategoria.selectedItem.isNullOrEmpty() -> {
                spCategoria.requestFocus()
                spCategoria.errorText = getString(R.string.rol_error_msg)
                false
            }
            else -> {
                if(!(radBtnA.isChecked || radBtnB.isChecked || radBtnC.isChecked || radBtnD.isChecked)){
                    tvErrorRadGroup.visibility = View.VISIBLE
                    false
                } else {
                    true
                }
            }
        }
    }
}