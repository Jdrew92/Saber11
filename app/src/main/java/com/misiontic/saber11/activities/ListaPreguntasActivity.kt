package com.misiontic.saber11.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.chivorn.smartmaterialspinner.SmartMaterialSpinner
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.misiontic.saber11.enums.Categoria
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.R
import com.misiontic.saber11.adapters.PreguntaAdapter
import com.misiontic.saber11.database.Saber11Database
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ListaPreguntasActivity : AppCompatActivity(), PreguntaAdapter.OnPreguntaClickListener {
    private lateinit var fabAddPregunta: FloatingActionButton
    private lateinit var adapter: PreguntaAdapter
    private lateinit var list: RecyclerView
    private lateinit var llm: LinearLayoutManager
    private lateinit var spFiltro: SmartMaterialSpinner<String>

    private var preguntas: ArrayList<Pregunta> = ArrayList()
    private var categorias: ArrayList<String> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_preguntas)

        val rol = intent.getStringExtra("rol")
        fabAddPregunta = findViewById(R.id.fabAddPregunta)
        setSupportActionBar(findViewById(R.id.toolbar))
        initSpinner()


        list = findViewById(R.id.list)
        adapter = PreguntaAdapter(preguntas, this, rol)

        llm = LinearLayoutManager(this)
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

        spFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (categorias[position].isEmpty()) {
                    spFiltro.clearSelection()
                }
                updateList(categorias[position])
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}

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

    private fun updateList(categoria: String = "") {
        val db = Saber11Database.getDatabase(this)
        val preguntaDao = db.preguntaDao()
        if (categoria.isEmpty()) {
            runBlocking {
                launch {
                    val result = preguntaDao.getAllPreguntas()
                    var i = 0
                    preguntas.clear()
                    while (i < result.size) {
                        preguntas.add(result[i])
                        i++
                    }
                    adapter.notifyDataSetChanged()
                }
            }
        } else {
            runBlocking {
                launch {
                    val result = preguntaDao.getPreguntasbyCategoria(categoria)
                    var i = 0
                    preguntas.clear()
                    while (i < result.size) {
                        preguntas.add(result[i])
                        i++
                    }
                    adapter.notifyDataSetChanged()
                }
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


    private fun initSpinner() {
        spFiltro = findViewById(R.id.spFiltro)
        categorias.add("")
        categorias.add(Categoria.LECTURA_CRITICA.value)
        categorias.add(Categoria.MATEMATICAS.value)
        categorias.add(Categoria.SOCIALES_CIUDADANAS.value)
        categorias.add(Categoria.CIENCIAS_NATURALES.value)
        categorias.add(Categoria.INGLES.value)
        spFiltro.item = categorias
    }

    override fun onPreguntaClick(id: Int, rol:String?) {
        val intent = Intent(this,  DetallePreguntaActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("rol", rol)
        val requestCode = 0
        startActivityForResult(intent, requestCode)
    }


}