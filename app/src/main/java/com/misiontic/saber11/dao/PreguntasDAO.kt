package com.misiontic.saber11.dao

import androidx.room.*
import com.misiontic.saber11.entities.Pregunta


@Dao
interface PreguntasDAO {
    //Generamos las operaciones de la BDs

    //Select
    @Query( "SELECT * FROM Pregunta")
    suspend fun getAllPreguntas() : List<Pregunta>

    @Query ( "SELECT * FROM Pregunta where categoria = :categoria")
    suspend fun getPreguntasbyCategoria(categoria: String) : List<Pregunta>

    @Query ( "SELECT * FROM Pregunta WHERE id = :id")
    suspend fun getPreguntabyId(id: Int) : Pregunta

    @Insert
    suspend fun insert(pregunta: Pregunta): Long

    @Update
    suspend fun actualizar(pregunta: Pregunta)

    @Delete
    suspend fun eliminar(pregunta: Pregunta)
}