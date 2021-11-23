package com.misiontic.saber11.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.R

class PreguntaAdapter(val preguntas:ArrayList<Pregunta>) :
    RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>() {

    class PreguntaViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreguntaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.preguntas_list_item, parent, false)
        return PreguntaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreguntaViewHolder, position: Int) {
        val tvDescripcion = holder.layout.findViewById<TextView>(R.id.tvDescripcionItem)
        val tvCategoria = holder.layout.findViewById<TextView>(R.id.tvCategoriaItem)
        tvDescripcion.text = preguntas[position].descripcion
        tvCategoria.text = preguntas[position].categoria
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }


}