package com.misiontic.saber11.entities

import java.io.Serializable

data class Usuario(
    val id: String? = null,
    val nombres:String? = null,
    val apellidos: String? = null,
    val telefono:String? = null,
    val email:String? = null,
    val password:String? = null,
    val rol:String? = null
): Serializable
