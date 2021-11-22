package com.misiontic.proyecto.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misiontic.proyecto.Entities.Pregunta
import com.misiontic.proyecto.R
import com.misiontic.proyecto.adapters.PreguntaAdapter
import com.misiontic.proyecto.database.Saber11Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaPreguntasActivity : AppCompatActivity() {
    private lateinit var fabAddPregunta: FloatingActionButton
    private lateinit var adapter: PreguntaAdapter
    private lateinit var list: RecyclerView
    private lateinit var llm: LinearLayoutManager

    private val data = Bundle()
    private var descripciones: ArrayList<String> = ArrayList()
    private var categorias: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_preguntas)
        val rol = intent.getStringExtra("rol")
        fabAddPregunta = findViewById(R.id.fabAddPregunta)
        setSupportActionBar(findViewById(R.id.toolbar))

        data.putStringArrayList("descripciones", descripciones)
        data.putStringArrayList("categorias", categorias)

        list = findViewById(R.id.list)
        adapter = PreguntaAdapter(data)

        llm = LinearLayoutManager(this)
        //list.setHasFixedSize(true)
        list.layoutManager = llm
        list.adapter = adapter
        list.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL))

        updateList()

        when (rol) {
            "Estudiante" -> {
                fabAddPregunta.visibility = View.GONE
            }
            "Docente" -> {
                fabAddPregunta.visibility = View.VISIBLE
                fabAddPregunta.setOnClickListener {
                    val intent = Intent(this, NewPreguntaActivity::class.java)
                    val requestCode = 0
                    intent.putExtra("rol", rol)
                    startActivityForResult(intent, requestCode)
                }
            }
            else -> {
                val intent = Intent(this, MainActivity::class.java)
                finish()
                startActivity(intent)
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.itemLogout -> {
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        super.onBackPressed()
    }

    private fun updateList() {
        val db = Saber11Database.getDatabase(this)
        val preguntaDao = db.preguntaDao()
        runBlocking {
            launch {
                val result = preguntaDao.getAllPreguntas()
                var i = 0
                descripciones.clear()
                categorias.clear()
                while (i < result.size) {
                    descripciones.add(result[i].descripcion)
                    categorias.add(result[i].categoria)
                    i++
                }
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                updateList()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

}