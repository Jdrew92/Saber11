package com.misiontic.saber11.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.misiontic.saber11.R
import com.misiontic.saber11.adapters.PreguntaAdapter
import com.misiontic.saber11.databinding.ActivityListaPreguntasBinding
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.entities.Usuario
import com.misiontic.saber11.enums.Categoria
import com.misiontic.saber11.enums.Rol
import com.misiontic.saber11.utils.Database


class ListaPreguntasActivity : AppCompatActivity(), PreguntaAdapter.OnPreguntaClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var adapter: PreguntaAdapter
    private lateinit var llm: LinearLayoutManager
    private lateinit var binding: ActivityListaPreguntasBinding
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var auth: FirebaseAuth

    private var preguntas: ArrayList<Pregunta> = ArrayList()
    private var categorias: ArrayList<Any> = ArrayList()
    private var filtradas: ArrayList<Pregunta> = ArrayList()

    private val tag = "ListaPreguntasActivity"
    private var user = mutableListOf<Usuario>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaPreguntasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar_list_test))

        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            binding.contentPreguntas.toolbarListPreguntas,
            R.string.drawer_open,
            R.string.drawer_closed
        )
        binding.drawerLayout.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)

        binding.navView.setNavigationItemSelectedListener(this)

        auth = Firebase.auth
        Firebase.initialize(this)
        user.clear()
        setRol()
        initSpinner()
        updateList()

        binding.contentPreguntas.spFiltro.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    position: Int,
                    id: Long
                ) {
                    if (categorias[position] == "") {
                        binding.contentPreguntas.spFiltro.clearSelection()
                    }
                    updateList(categorias[position].toString())
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}

            }

    }

    override fun onPostCreate(savedInstanceState: Bundle?) {
        super.onPostCreate(savedInstanceState)
        toggle.syncState()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        toggle.onConfigurationChanged(newConfig)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when(item.itemId){
            R.id.app_bar_search -> {
                val buscadas = mutableListOf<Pregunta>()
                val buscador = item
                Log.i("buscador", buscador.actionProvider.toString())
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

    //Funciones de Interfaces

    override fun onPreguntaClick(pregunta: Pregunta) {
        val intent = Intent(this, DetallePreguntaActivity::class.java)
        intent.putExtra("usuario", user[0])
        intent.putExtra("pregunta", pregunta)
        startActivity(intent)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.test_navItem -> {
                val intent = Intent(this, ListaTestsActivity::class.java)
                intent.putExtra("usuario", user[0])
                finish()
                startActivity(intent)
            }
            R.id.itemLogout -> {
                Firebase.auth.signOut()
                finish()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
        binding.drawerLayout.closeDrawer(GravityCompat.START)
        return true
    }


    //funciones privadas de operacion

    private fun setRol() {
        val usuarioListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val userMap = snapshot.value as Map<*, *>
                val usuario = Usuario(
                    userMap["id"].toString(),
                    userMap["nombres"].toString(),
                    userMap["apellidos"].toString(),
                    null, null, null,
                    userMap["rol"].toString()
                )
                user.add(usuario)
                when (usuario.rol) {
                    Rol.DOCENTE.value -> {
                        binding.contentPreguntas.fabAddPregunta.visibility = View.VISIBLE
                        binding.contentPreguntas.fabAddPregunta.setOnClickListener {
                            val intent =
                                Intent(this@ListaPreguntasActivity, NewPreguntaActivity::class.java)
                            intent.putExtra("usuario", usuario)
                            startActivity(intent)
                        }
                    }
                    else -> {
                        binding.contentPreguntas.fabAddPregunta.visibility = View.GONE
                    }
                }
                findViewById<TextView>(R.id.tvNombreNav).text = getString(
                    R.string.nombre_usuario,
                    usuario.nombres,
                    usuario.apellidos
                )
                findViewById<TextView>(R.id.tvRolNav).text =
                    getString(R.string.rol_usuario, usuario.rol)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(tag, "onCancelled updateList: ", error.toException())
            }

        }
        Database.getUsuariosReference().child(auth.currentUser!!.uid)
            .addListenerForSingleValueEvent(usuarioListener)
    }

    private fun updateList(categoria: String = "") {
        val preguntasItemListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                preguntas.clear()
                for (p in snapshot.children) {
                    val mapPregunta = p.value as Map<*, *>

                    val pregunta = Pregunta(
                        mapPregunta["id"].toString(),
                        mapPregunta["descripcion"].toString(),
                        mapPregunta["respuesta1"].toString(),
                        mapPregunta["respuesta2"].toString(),
                        mapPregunta["respuesta3"].toString(),
                        mapPregunta["respuesta4"].toString(),
                        mapPregunta["respuestaCorrecta"].toString(),
                        mapPregunta["opcionCorrecta"].toString(),
                        null,
                        mapPregunta["categoria"].toString()
                    )
                    preguntas.add(pregunta)
                }
                println("Size:${preguntas.size} Categoria:$categoria")
                filtradas = aplicarFiltro(categoria, preguntas)
                adapter = PreguntaAdapter(filtradas, this@ListaPreguntasActivity, this@ListaPreguntasActivity)
                llm = LinearLayoutManager(this@ListaPreguntasActivity)
                binding.contentPreguntas.list.layoutManager = llm
                binding.contentPreguntas.list.adapter = adapter
                binding.contentPreguntas.list.addItemDecoration(
                    DividerItemDecoration(
                        this@ListaPreguntasActivity,
                        DividerItemDecoration.HORIZONTAL
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(tag, "onCancelled updateList: ", error.toException())
            }
        }
        Database.getPreguntasReference().addValueEventListener(preguntasItemListener)
    }


    private fun initSpinner() {
        categorias.add("")
        for (categoria in Categoria.values()) {
            categorias.add(categoria.value)
        }
        binding.contentPreguntas.spFiltro.item = categorias
    }

    private fun aplicarFiltro(
        categoria: String,
        preguntas: ArrayList<Pregunta>
    ): ArrayList<Pregunta> {
        val list = ArrayList<Pregunta>()
        return if (categoria.isEmpty()) {
            preguntas
        } else {
            for (p in preguntas) {
                if (p.categoria == categoria) {
                    list.add(p)
                }
            }
            list
        }
    }

}