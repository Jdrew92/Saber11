package com.misiontic.saber11.utils

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class Database {
    companion object{
        private val database = Firebase.database
        private val usuariosRef = database.getReference("Usuarios")
        private val preguntasRef = database.getReference("Preguntas")
        private val testsRef = database.getReference("Tests")
        fun getUsuariosReference() = usuariosRef
        fun getPreguntasReference() = preguntasRef
        fun getTestsReference() = testsRef
    }

}