package com.misiontic.saber11.entities

import androidx.room.*

@Entity
data class Prueba(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val calificacion: Int
)
