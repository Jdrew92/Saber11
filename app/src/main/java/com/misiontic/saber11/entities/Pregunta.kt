package com.misiontic.saber11.entities

import java.io.Serializable

data class Pregunta(
    val id:String? = null,
    val descripcion:String? = null,
    val respuesta1:String? = null,
    val respuesta2:String? = null,
    val respuesta3:String? = null,
    val respuesta4:String? = null,
    val respuestaCorrecta:String? = null,
    val opcionCorrecta:String? = null,
    var marcada:String?=null,
    val categoria:String? = null
) : Serializable
