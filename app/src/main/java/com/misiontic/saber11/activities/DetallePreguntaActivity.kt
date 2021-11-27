package com.misiontic.saber11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Toast
import com.misiontic.saber11.R
import com.misiontic.saber11.database.Saber11Database
import com.misiontic.saber11.databinding.ActivityDetallePreguntaBinding
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.enums.Categoria
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class DetallePreguntaActivity : AppCompatActivity() {

    private var categorias: ArrayList<Any> = ArrayList()
    private lateinit var detalleBinding: ActivityDetallePreguntaBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detalleBinding = ActivityDetallePreguntaBinding.inflate(layoutInflater)
        setContentView(detalleBinding.root)
        initSpinner()
        val id = intent.getIntExtra("id", 1)
        requestData(id)

        detalleBinding.edtDescripcionDetalle.inputType = InputType.TYPE_NULL
        detalleBinding.edtRespuesta1Detalle.inputType = InputType.TYPE_NULL
        detalleBinding.edtRespuesta2Detalle.inputType = InputType.TYPE_NULL
        detalleBinding.edtRespuesta3Detalle.inputType = InputType.TYPE_NULL
        detalleBinding.edtRespuesta4Detalle.inputType = InputType.TYPE_NULL
        detalleBinding.radBtnADetalle.isEnabled = false
        detalleBinding.radBtnBDetalle.isEnabled = false
        detalleBinding.radBtnCDetalle.isEnabled = false
        detalleBinding.radBtnDDetalle.isEnabled = false
        detalleBinding.tvErrorRadGroupDetalle.visibility = View.GONE
        detalleBinding.spCategoriaDetalle.isEnabled = false

        //Botón Editar-Guardar
        detalleBinding.btnEditar.setOnClickListener {
            when (detalleBinding.btnEditar.text.toString()) {
                getString(R.string.editar_pregunta) -> {
                    detalleBinding.edtDescripcionDetalle.inputType =
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    detalleBinding.edtRespuesta1Detalle.inputType =
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    detalleBinding.edtRespuesta2Detalle.inputType =
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    detalleBinding.edtRespuesta3Detalle.inputType =
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    detalleBinding.edtRespuesta4Detalle.inputType =
                        InputType.TYPE_TEXT_VARIATION_LONG_MESSAGE;InputType.TYPE_TEXT_FLAG_AUTO_CORRECT;InputType.TYPE_TEXT_FLAG_MULTI_LINE
                    detalleBinding.radBtnADetalle.isEnabled = true
                    detalleBinding.radBtnBDetalle.isEnabled = true
                    detalleBinding.radBtnCDetalle.isEnabled = true
                    detalleBinding.radBtnDDetalle.isEnabled = true
                    detalleBinding.spCategoriaDetalle.isEnabled = true
                    detalleBinding.btnEditar.text = getString(R.string.guardar_cambios)
                    detalleBinding.btnBack.text = getString(R.string.cancelar)
                }
                getString(R.string.guardar_cambios) -> {
                    if (allInfoIsFilled()) {
                        val db = Saber11Database.getDatabase(this)
                        val preguntaDao = db.preguntaDao()
                        val correcta =
                            when (detalleBinding.rdGroupRespuestasDetalle.checkedRadioButtonId) {
                                R.id.radBtnADetalle -> getString(R.string.option_a)
                                R.id.radBtnBDetalle -> getString(R.string.option_b)
                                R.id.radBtnCDetalle -> getString(R.string.option_c)
                                else -> getString(R.string.option_d)
                            }
                        val pregunta = Pregunta(
                            id,
                            detalleBinding.edtDescripcionDetalle.text.toString(),
                            detalleBinding.edtRespuesta1Detalle.text.toString(),
                            detalleBinding.edtRespuesta2Detalle.text.toString(),
                            detalleBinding.edtRespuesta3Detalle.text.toString(),
                            detalleBinding.edtRespuesta4Detalle.text.toString(),
                            correcta,
                            detalleBinding.spCategoriaDetalle.selectedItem.toString())
                        runBlocking {
                            launch {
                                val result = preguntaDao.actualizar(pregunta)
                                if(result != -1){
                                    Toast.makeText(this@DetallePreguntaActivity, "Se guardaron los cambios con éxito!", Toast.LENGTH_LONG).show()
                                    val intent = Intent(this@DetallePreguntaActivity, ListaPreguntasActivity::class.java)
                                    intent.putExtra("rol", getIntent().getStringExtra("rol"))
                                    setResult(RESULT_OK, intent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
        }
        //Botón Cancelar-Regresar
        detalleBinding.btnBack.setOnClickListener {
            when(detalleBinding.btnBack.text.toString()) {
                getString(R.string.cancelar) -> {
                    detalleBinding.edtDescripcionDetalle.inputType = InputType.TYPE_NULL
                    detalleBinding.edtRespuesta1Detalle.inputType = InputType.TYPE_NULL
                    detalleBinding.edtRespuesta2Detalle.inputType = InputType.TYPE_NULL
                    detalleBinding.edtRespuesta3Detalle.inputType = InputType.TYPE_NULL
                    detalleBinding.edtRespuesta4Detalle.inputType = InputType.TYPE_NULL
                    detalleBinding.radBtnADetalle.isEnabled = false
                    detalleBinding.radBtnBDetalle.isEnabled = false
                    detalleBinding.radBtnCDetalle.isEnabled = false
                    detalleBinding.radBtnDDetalle.isEnabled = false
                    detalleBinding.tvErrorRadGroupDetalle.visibility = View.GONE
                    detalleBinding.spCategoriaDetalle.isEnabled = false
                    detalleBinding.btnEditar.text = getString(R.string.editar_pregunta)
                    detalleBinding.btnBack.text = getString(R.string.regresar)
                }
                getString(R.string.regresar) -> {
                    val intent = Intent(this, ListaPreguntasActivity::class.java)
                    intent.putExtra("rol", getIntent().getStringExtra("rol"))
                    finish()
                    startActivity(intent)
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
        detalleBinding.spCategoriaDetalle.item = categorias
    }

    private fun requestData(id: Int) {
        val db = Saber11Database.getDatabase(this)
        val preguntasDao = db.preguntaDao()
        runBlocking {
            launch {
                val result = preguntasDao.getPreguntabyId(id)
                detalleBinding.edtDescripcionDetalle.setText(result.descripcion)
                detalleBinding.edtRespuesta1Detalle.setText(result.respuesta1)
                detalleBinding.edtRespuesta2Detalle.setText(result.respuesta2)
                detalleBinding.edtRespuesta3Detalle.setText(result.respuesta3)
                detalleBinding.edtRespuesta4Detalle.setText(result.respuesta4)
                val radioButton = when (result.correcta) {
                    getString(R.string.option_a) -> R.id.radBtnADetalle
                    getString(R.string.option_b) -> R.id.radBtnBDetalle
                    getString(R.string.option_c) -> R.id.radBtnCDetalle
                    else -> R.id.radBtnDDetalle
                }
                detalleBinding.rdGroupRespuestasDetalle.check(radioButton)
                detalleBinding.spCategoriaDetalle.setSelection(categorias.indexOf(result.categoria))
            }
        }
    }

    private fun allInfoIsFilled(): Boolean {
        return when {
            detalleBinding.edtDescripcionDetalle.text.isNullOrEmpty() -> {
                detalleBinding.edtDescripcionDetalle.requestFocus()
                detalleBinding.edtDescripcionDetalle.error = getString(R.string.blank_error_msg)
                false
            }
            detalleBinding.edtRespuesta1Detalle.text.isNullOrEmpty() -> {
                detalleBinding.edtRespuesta1Detalle.requestFocus()
                detalleBinding.edtRespuesta1Detalle.error = getString(R.string.blank_error_msg)
                false
            }
            detalleBinding.edtRespuesta2Detalle.text.isNullOrEmpty() -> {
                detalleBinding.edtRespuesta2Detalle.requestFocus()
                detalleBinding.edtRespuesta2Detalle.error = getString(R.string.blank_error_msg)
                false
            }
            detalleBinding.edtRespuesta3Detalle.text.isNullOrEmpty() -> {
                detalleBinding.edtRespuesta3Detalle.requestFocus()
                detalleBinding.edtRespuesta3Detalle.error = getString(R.string.blank_error_msg)
                false
            }
            detalleBinding.edtRespuesta4Detalle.text.isNullOrEmpty() -> {
                detalleBinding.edtRespuesta4Detalle.requestFocus()
                detalleBinding.edtRespuesta4Detalle.error = getString(R.string.blank_error_msg)
                false
            }
            detalleBinding.spCategoriaDetalle.selectedItem.toString().isEmpty() -> {
                detalleBinding.spCategoriaDetalle.requestFocus()
                detalleBinding.spCategoriaDetalle.errorText = getString(R.string.rol_error_msg)
                false
            }
            else -> {
                if (!(detalleBinding.radBtnADetalle.isChecked ||
                            detalleBinding.radBtnBDetalle.isChecked ||
                            detalleBinding.radBtnCDetalle.isChecked ||
                            detalleBinding.radBtnDDetalle.isChecked)
                ) {
                    detalleBinding.tvErrorRadGroupDetalle.visibility = View.VISIBLE
                    false
                } else {
                    true
                }
            }
        }
    }
}