package com.misiontic.saber11.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.R
import com.misiontic.saber11.activities.DetallePreguntaActivity
import com.misiontic.saber11.activities.MainActivity

class PreguntaAdapter(val preguntas:ArrayList<Pregunta>, val clickListener:OnPreguntaClickListener, val rol:String?) :
    RecyclerView.Adapter<PreguntaAdapter.PreguntaViewHolder>(){

    class PreguntaViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    interface OnPreguntaClickListener {
        fun onPreguntaClick(id: Int, rol:String?)
    }

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
        holder.layout.setOnClickListener { clickListener.onPreguntaClick(preguntas[position].id, rol) }
    }

    override fun getItemCount(): Int {
        return preguntas.size
    }


}