package com.misiontic.saber11.entities

import java.io.Serializable

data class Pregunta(
    val id:String? = null,
    val descripcion:String? = null,
    val respuesta1:String? = null,
    val respuesta2:String? = null,
    val respuesta3:String? = null,
    val respuesta4:String? = null,
    val correcta:String? = null,
    val marcada:String?=null,
    val categoria:String? = null
) : Serializable
