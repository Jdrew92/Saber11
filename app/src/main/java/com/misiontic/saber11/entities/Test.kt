package com.misiontic.saber11.entities

import java.io.Serializable

data class Test(
    val id: String? = null,
    val fecha: String? = null,
    val preguntas: List<Pregunta>? = null,
    val calificacion: Any? = null
): Serializable
