package com.misiontic.saber11.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val nombres:String?,
    val apellidos: String?,
    val telefono:String?,
    val email:String?,
    val password:String?,
    val rol:String?
)
