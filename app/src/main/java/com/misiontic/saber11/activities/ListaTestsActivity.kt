package com.misiontic.saber11.activities

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.misiontic.saber11.adapters.TestAdapter
import com.misiontic.saber11.databinding.ActivityListaTestsBinding
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.entities.Test
import com.misiontic.saber11.entities.Usuario
import com.misiontic.saber11.utils.Database
import java.text.SimpleDateFormat
import java.util.*

class ListaTestsActivity : AppCompatActivity(), TestAdapter.OnTestClickListener,
    NavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityListaTestsBinding
    private lateinit var adapter: TestAdapter
    private lateinit var llm: LinearLayoutManager

    private lateinit var auth: FirebaseAuth
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var usuario: Usuario
    private var tests = mutableListOf<Test>()
    private var preguntas = mutableListOf<Pregunta>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListaTestsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(findViewById(R.id.toolbar_detalle))
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayoutTest,
            binding.contentTests.toolbarListTest,
            R.string.drawer_open,
            R.string.drawer_closed
        )
        binding.drawerLayoutTest.addDrawerListener(toggle)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.title = getString(R.string.lista_de_test)
        binding.navViewTest.setNavigationItemSelectedListener(this)

        auth = Firebase.auth
        Firebase.initialize(this)
        usuario = intent.extras?.get("usuario") as Usuario
        Log.i("ListaTest", usuario.nombres.toString())

        binding.navViewTest.getHeaderView(0).findViewById<TextView>(R.id.tvNombreNav).text =
            getString(
                R.string.nombre_usuario,
                usuario.nombres,
                usuario.apellidos
            )
        binding.navViewTest.getHeaderView(0).findViewById<TextView>(R.id.tvRolNav).text =
            getString(R.string.rol_usuario, usuario.rol)

        updatePreguntas()
        updateList()

        binding.contentTests.fabNewTest.setOnClickListener { newTest() }
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
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val intent = Intent(Intent.ACTION_MAIN)
        intent.addCategory(Intent.CATEGORY_HOME)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        super.onBackPressed()
    }

    //Funciones Interfaces
    override fun onTestClick(test: Test) {
        toCuestionario(test)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.pregunta_navItem -> {
                val intent = Intent(this, ListaPreguntasActivity::class.java)
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
        binding.drawerLayoutTest.closeDrawer(GravityCompat.START)
        return true
    }

    private fun updateList() {
        val preguntasItemListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tests.clear()
                for (t in snapshot.children) {
                    val mapTest = t.value as Map<*, *>
                    val preguntasGetted = mapTest["preguntas"] as List<*>
                    val preguntas = mutableListOf<Pregunta>()
                    for (p in preguntasGetted) {
                        val mapP = p as Map<*, *>
                        val pregunta =
                            Pregunta(
                                mapP["id"].toString(),
                                mapP["descripcion"].toString(),
                                mapP["respuesta1"].toString(),
                                mapP["respuesta2"].toString(),
                                mapP["respuesta3"].toString(),
                                mapP["respuesta4"].toString(),
                                mapP["respuestaCorrecta"].toString(),
                                mapP["opcionCorrecta"].toString(),
                                mapP["marcada"].toString(),
                                mapP["categoria"].toString()
                            )
                        preguntas.add(pregunta)
                    }
                    val test = Test(
                        mapTest["id"].toString(),
                        mapTest["fecha"].toString(),
                        preguntas,
                        mapTest["calificacion"].toString()

                    )
                    tests.add(test)
                }
                println("Size:${tests.size}")
                adapter = TestAdapter(tests, this@ListaTestsActivity)
                llm = LinearLayoutManager(this@ListaTestsActivity)
                binding.contentTests.testList.layoutManager = llm
                binding.contentTests.testList.adapter = adapter
                binding.contentTests.testList.addItemDecoration(
                    DividerItemDecoration(
                        this@ListaTestsActivity,
                        DividerItemDecoration.HORIZONTAL
                    )
                )
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ListaTests", "onCancelled updateList: ", error.toException())
            }
        }
        Database.getTestsReference().child(auth.currentUser!!.uid)
            .addValueEventListener(preguntasItemListener)
    }

    private fun updatePreguntas() {
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
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ListaTests", "onCancelled updatePreguntas: ", error.toException())
            }
        }
        Database.getPreguntasReference().addValueEventListener(preguntasItemListener)
    }

    private fun newTest() {
        //Esto debe cambiar a que actualice las preguntas desde la nube
        val preguntasTest = mutableListOf<Pregunta>()
        val listaPreguntas = preguntas
        for (i in 0..4) {
            val range = 0 until listaPreguntas.size
            val random = range.random()
            preguntasTest.add(listaPreguntas.removeAt(random))
            preguntasTest[i].marcada = ""
        }
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val currentDate = sdf.format(Date())
        val test = Test(UUID.randomUUID().toString(), currentDate, preguntasTest, "")
        Database.getTestsReference().child(auth.currentUser!!.uid).child(test.id!!).setValue(test)
        toCuestionario(test)

    }

    private fun toCuestionario(test: Test) {
        val intent = Intent(this, CuestionarioActivity::class.java)
        intent.putExtra("usuario", usuario)
        intent.putExtra("test", test)
        startActivity(intent)
    }
}