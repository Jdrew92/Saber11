package com.misiontic.saber11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.misiontic.saber11.R
import com.misiontic.saber11.databinding.ActivityDetallePreguntaBinding
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.enums.Categoria
import com.misiontic.saber11.enums.Rol
import com.misiontic.saber11.utils.Database

class DetallePreguntaActivity : AppCompatActivity() {

    private var categorias: ArrayList<Any> = ArrayList()
    private lateinit var detalleBinding: ActivityDetallePreguntaBinding
    private lateinit var auth: FirebaseAuth

    private val TAG = "DetallePreguntaACtivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        detalleBinding = ActivityDetallePreguntaBinding.inflate(layoutInflater)
        setContentView(detalleBinding.root)

        auth = Firebase.auth
        Firebase.initialize(this)

        initSpinner()
        val pregunta = intent.extras!!.get("pregunta") as Pregunta
        requestData(pregunta)

        disableAll()
        setRol()


        //Botón Editar-Guardar
        detalleBinding.btnEditar.setOnClickListener {
            when (detalleBinding.btnEditar.text.toString()) {
                getString(R.string.editar_pregunta) -> {
                    enableAll()
                    detalleBinding.btnEditar.text = getString(R.string.guardar_cambios)
                    detalleBinding.btnBack.text = getString(R.string.cancelar)
                }
                getString(R.string.guardar_cambios) -> {
                    if (allInfoIsFilled()) {

                        val correcta =
                            when (detalleBinding.rdGroupRespuestasDetalle.checkedRadioButtonId) {
                                R.id.radBtnADetalle -> getString(R.string.option_a)
                                R.id.radBtnBDetalle -> getString(R.string.option_b)
                                R.id.radBtnCDetalle -> getString(R.string.option_c)
                                else -> getString(R.string.option_d)
                            }
                        val p = Pregunta(
                            pregunta.id,
                            detalleBinding.edtDescripcionDetalle.text.toString(),
                            detalleBinding.edtRespuesta1Detalle.text.toString(),
                            detalleBinding.edtRespuesta2Detalle.text.toString(),
                            detalleBinding.edtRespuesta3Detalle.text.toString(),
                            detalleBinding.edtRespuesta4Detalle.text.toString(),
                            correcta,
                            detalleBinding.spCategoriaDetalle.selectedItem.toString()
                        )
                        Database.getPreguntasReference().child(p.id!!).setValue(p)
                        Toast.makeText(this, "Se actualizó la pregunta", Toast.LENGTH_LONG).show()
                        toListaPreguntas()
                    }
                }
            }
        }
        //Botón Cancelar-Regresar
        detalleBinding.btnBack.setOnClickListener {
            when (detalleBinding.btnBack.text.toString()) {
                getString(R.string.cancelar) -> {
                    disableAll()
                    detalleBinding.btnEditar.text = getString(R.string.editar_pregunta)
                    detalleBinding.btnBack.text = getString(R.string.regresar)
                }
                getString(R.string.regresar) -> {
                    toListaPreguntas()
                }
            }
        }
    }

    private fun toListaPreguntas() {
        val intent = Intent(this, ListaPreguntasActivity::class.java)
        finish()
        startActivity(intent)
    }


    private fun initSpinner() {
        for (categoria in Categoria.values()) {
            categorias.add(categoria.value)
        }
        detalleBinding.spCategoriaDetalle.item = categorias
    }

    private fun requestData(pregunta: Pregunta) {
                detalleBinding.edtDescripcionDetalle.setText(pregunta.descripcion)
                detalleBinding.edtRespuesta1Detalle.setText(pregunta.respuesta1)
                detalleBinding.edtRespuesta2Detalle.setText(pregunta.respuesta2)
                detalleBinding.edtRespuesta3Detalle.setText(pregunta.respuesta3)
                detalleBinding.edtRespuesta4Detalle.setText(pregunta.respuesta4)
                val correcta = when(pregunta.correcta){
                    getString(R.string.option_a)->R.id.radBtnADetalle
                    getString(R.string.option_b)->R.id.radBtnBDetalle
                    getString(R.string.option_c)->R.id.radBtnCDetalle
                    else->R.id.radBtnDDetalle
                }
                detalleBinding.rdGroupRespuestasDetalle.check(correcta)
                detalleBinding.spCategoriaDetalle.setSelection(categorias.indexOf(pregunta.categoria.toString()))

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

    private fun enableAll() {
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
    }

    private fun disableAll() {
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
    }

    private fun setRol() {
        val usuarioListener = object:ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val usuario = snapshot.value as Map<*,*>
                when (usuario["rol"].toString()) {
                    Rol.DOCENTE.value -> {
                        detalleBinding.btnEditar.visibility = View.VISIBLE
                        detalleBinding.btnBack.visibility = View.VISIBLE
                    }
                    else -> {
                        detalleBinding.btnEditar.visibility = View.GONE
                        detalleBinding.btnBack.visibility = View.GONE
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Log.e(TAG, "onCancelled updateList: ", error.toException())
            }

        }
        Database.getUsuariosReference().child(auth.currentUser!!.uid).addListenerForSingleValueEvent(usuarioListener)
    }
}