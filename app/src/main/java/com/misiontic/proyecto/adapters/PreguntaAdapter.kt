package com.misiontic.proyecto.adapters

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.proyecto.Entities.Pregunta
import com.misiontic.proyecto.R

class PreguntaAdapter(val data: Bundle) :
    RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>() {

    class PreguntaViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    private var descripciones = data.getStringArrayList("descripciones") as ArrayList<String>
    private var categorias = data.getStringArrayList("categorias") as ArrayList<String>

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreguntaViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.preguntas_list_item, parent, false)
        return PreguntaViewHolder(view)
    }

    override fun onBindViewHolder(holder: PreguntaViewHolder, position: Int) {
        val tvDescripcion = holder.layout.findViewById<TextView>(R.id.tvDescripcionItem)
        val tvCategoria = holder.layout.findViewById<TextView>(R.id.tvCategoriaItem)
        tvDescripcion.text = descripciones[position]
        tvCategoria.text = categorias[position]
    }

    override fun getItemCount(): Int {
        return descripciones.size
    }


}