package com.misiontic.saber11.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.misiontic.saber11.adapters.CuestionarioAdapter
import com.misiontic.saber11.databinding.ActivityCuestionarioBinding
import com.misiontic.saber11.entities.Test

class CuestionarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCuestionarioBinding
    private lateinit var adapter: CuestionarioAdapter
    private lateinit var llm: LinearLayoutManager

    private lateinit var auth: FirebaseAuth
    private lateinit var test: Test
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCuestionarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        Firebase.initialize(this)

        test = intent.extras?.get("test") as Test
        adapter = CuestionarioAdapter(test.preguntas!!, test)
        llm = LinearLayoutManager(this)
        binding.lstCuestionario.layoutManager = llm
        binding.lstCuestionario.adapter = adapter
        binding.lstCuestionario.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.HORIZONTAL
            )
        )
    }
}