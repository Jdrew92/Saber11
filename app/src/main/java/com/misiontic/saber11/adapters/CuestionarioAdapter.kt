package com.misiontic.saber11.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.R
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.entities.Test

class CuestionarioAdapter(
    val preguntas: List<Pregunta>, test: Test
) : RecyclerView.Adapter<CuestionarioAdapter.CuestionarioViewHolder>() {

    class CuestionarioViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CuestionarioViewHolder {
        val myLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.cuestionario_item, parent, false)
        return CuestionarioViewHolder(myLayout)
    }

    override fun onBindViewHolder(holder: CuestionarioViewHolder, position: Int) {
        val tvDescripcion = holder.layout.findViewById<TextView>(R.id.tvDescrpCuestionario)
        val rdBtnA = holder.layout.findViewById<TextView>(R.id.rbRespuestaA)
        val rdBtnB = holder.layout.findViewById<TextView>(R.id.rbRespuestaB)
        val rdBtnC = holder.layout.findViewById<TextView>(R.id.rbRespuestaC)
        val rdBtnD = holder.layout.findViewById<TextView>(R.id.rbRespuestaD)
        tvDescripcion.text = preguntas[position].descripcion
        rdBtnA.text = preguntas[position].respuesta1
        rdBtnB.text = preguntas[position].respuesta2
        rdBtnC.text = preguntas[position].respuesta3
        rdBtnD.text = preguntas[position].respuesta4
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }

}