package com.misiontic.saber11.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.misiontic.saber11.R
import com.misiontic.saber11.entities.Test

class TestAdapter(private val testList: MutableList<Test>, private val clickListener: OnTestClickListener) :
    RecyclerView.Adapter<TestAdapter.TestViewHolder>() {

    class TestViewHolder(val layout: View) : RecyclerView.ViewHolder(layout)

    interface OnTestClickListener {
        fun onTestClick(test: Test)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TestViewHolder {
        val myLayout =
            LayoutInflater.from(parent.context).inflate(R.layout.test_list_item, parent, false)
        return TestViewHolder(myLayout)
    }

    override fun onBindViewHolder(holder: TestViewHolder, position: Int) {
        val tvTitle = holder.layout.findViewById<TextView>(R.id.tvTestTitleItem)
        val tvFecha = holder.layout.findViewById<TextView>(R.id.tvTestFechaItem)
        val tvCalificacion = holder.layout.findViewById<TextView>(R.id.tvCalificacionItem)

        tvTitle.text = holder.layout.context.getString(R.string.cuestionario, position + 1)
        tvFecha.text = holder.layout.context.getString(R.string.fecha, testList[position].fecha)
        tvCalificacion.text =
            if (testList[position].calificacion != "") holder.layout.context.getString(
                R.string.calificacion,
                testList[position].calificacion.toString()
            )
            else holder.layout.context.getString(R.string.test_sin_realizar)

        holder.layout.setOnClickListener { clickListener.onTestClick(testList[position]) }
    }

    override fun getItemCount(): Int {
        return testList.size
    }


}