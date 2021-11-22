package com.misiontic.proyecto.Entities

import androidx.room.*

@Entity
data class Pregunta(
    @PrimaryKey(autoGenerate = true) val id:Int,
    val descripcion:String,
    val respuesta1:String,
    val respuesta2:String,
    val respuesta3:String,
    val respuesta4:String,
    val correcta:String,
    val categoria:String
)
