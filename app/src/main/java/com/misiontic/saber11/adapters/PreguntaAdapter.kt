package com.misiontic.saber11.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.R
import com.misiontic.saber11.entities.Pregunta

class PreguntaAdapter(
    private val preguntas: ArrayList<Pregunta>,
    private val clickListener: OnPreguntaClickListener,
    val context: Context
) :
    RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>() {

    class PreguntaViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    interface OnPreguntaClickListener {
        fun onPreguntaClick(pregunta: Pregunta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreguntaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.preguntas_list_item, parent, false)
        return PreguntaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreguntaViewHolder, position: Int) {
        val tvDescripcion = holder.layout.findViewById<TextView>(R.id.tvDescripcionItem)
        val tvCategoria = holder.layout.findViewById<TextView>(R.id.tvCategoriaItem)
        val tvRespuesta = holder.layout.findViewById<TextView>(R.id.tvRespuestaItem)
        tvDescripcion.text = preguntas[position].descripcion
        tvCategoria.text = preguntas[position].categoria
        tvRespuesta.text =
            context.getString(R.string.respuesta, preguntas[position].respuestaCorrecta)
        holder.layout.setOnClickListener { clickListener.onPreguntaClick(preguntas[position]) }
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }


}