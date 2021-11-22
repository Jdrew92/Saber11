import com.misiontic.proyecto.Entities.Pregunta
import com.misiontic.proyecto.R

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter

class PreguntasAdapter(private val mContext: Context, val listaPreguntas : List<Pregunta>)
    : ArrayAdapter<Pregunta>( mContext, 0, listaPreguntas) {

    //Posicion      Elemento     Parent
//    override fun getView(posicion: Int, view: View?, viewGroup: ViewGroup): View {
//        val layout =  LayoutInflater.from(mContext).inflate( R.layout.preguntas_list_item, viewGroup, false )
//        val pregunta =  listaPreguntas[posicion]
//        val Titulo = layout.findViewById<TextView>( R.id.Titulo)
//        val nPregunta = layout.findViewById<TextView>( R.id.Titulo)
//        val nRespuesta = layout.findViewById<TextView>( R.id.Titulo)
//
//        Titulo.text = pregunta.titulo
//        nPregunta.text = "${pregunta.npregunta}"  // pregunta.npregunta.toString()
//        nRespuesta.text = pregunta.materia
//
//        return layout
//    }

    /*override fun getView(posicion: Int, view: View?, viewGroup: ViewGroup): View {
        val layout =  LayoutInflater.from(mContext).inflate( R.layout.preguntas_list_item, viewGroup, false )
        val pregunta =  listaPreguntas[posicion]

        layout.
        layout.nPregunta.text = "${pregunta.npregunta}"
        layout.nRespuesta.text = pregunta.materia

        return layout
    }*/
}