package com.misiontic.saber11.adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.R
import com.misiontic.saber11.entities.Test

class CuestionarioAdapter(
    val test: Test, val context: Context
) : RecyclerView.Adapter<CuestionarioAdapter.CuestionarioViewHolder>() {

    class CuestionarioViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuestionarioViewHolder {
        val myLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.cuestionario_item, parent, false)
        return CuestionarioViewHolder(myLayout)
    }

    override fun onBindViewHolder(holder: CuestionarioViewHolder, position: Int) {
        val tvDescripcion = holder.layout.findViewById<TextView>(R.id.tvDescrpCuestionario)
        val rdGroup = holder.layout.findViewById<RadioGroup>(R.id.rdGpCuestionario)
        val rdBtnA = holder.layout.findViewById<TextView>(R.id.rbRespuestaA)
        val rdBtnB = holder.layout.findViewById<TextView>(R.id.rbRespuestaB)
        val rdBtnC = holder.layout.findViewById<TextView>(R.id.rbRespuestaC)
        val rdBtnD = holder.layout.findViewById<TextView>(R.id.rbRespuestaD)
        tvDescripcion.text = test.preguntas?.get(position)?.descripcion
        rdBtnA.text = context.getString(R.string.answer_a, test.preguntas?.get(position)?.respuesta1)
        rdBtnB.text = context.getString(R.string.answer_b,test.preguntas?.get(position)?.respuesta2)
        rdBtnC.text = context.getString(R.string.answer_c,test.preguntas?.get(position)?.respuesta3)
        rdBtnD.text = context.getString(R.string.answer_d, test.preguntas?.get(position)?.respuesta4)
        if (!test.preguntas?.get(position)?.marcada.isNullOrEmpty()) {
            val checked = when (test.preguntas?.get(position)?.marcada) {
                context.getString(R.string.option_a) -> R.id.rbRespuestaA
                context.getString(R.string.option_b) -> R.id.rbRespuestaB
                context.getString(R.string.option_c) -> R.id.rbRespuestaC
                context.getString(R.string.option_d) -> R.id.rbRespuestaD
                else -> -1
            }
            Log.d("Adapter", "checked: $checked")
            rdGroup.check(checked)
        }
        rdGroup.setOnCheckedChangeListener { _, checkedId ->
            Log.d("Adapter", "checkedId: $checkedId")
            test.preguntas?.get(position)?.marcada = when(checkedId){
                R.id.rbRespuestaA -> context.getString(R.string.option_a)
                R.id.rbRespuestaB -> context.getString(R.string.option_b)
                R.id.rbRespuestaC -> context.getString(R.string.option_c)
                R.id.rbRespuestaD -> context.getString(R.string.option_d)
                else -> ""
            }
        }


    }

    override fun getItemCount(): Int {
        return test.preguntas!!.size
    }

    fun obtenerTest(): Test {
        return test
    }
}