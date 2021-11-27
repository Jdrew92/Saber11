package com.misiontic.saber11.dao
import androidx.room.*
import com.misiontic.saber11.entities.Usuario

@Dao
interface UsuarioDAO {
    @Query("SELECT * FROM Usuario")
    suspend fun getAllUsuarios(): List<Usuario>

    @Query("SELECT * FROM Usuario WHERE email = :email AND password = :password")
    suspend fun getUsuarioByEmailAndPassword(email: String, password: String): List<Usuario>

    @Query("SELECT email FROM Usuario WHERE email = :email")
    suspend fun getUsuarioByEmail(email: String): String

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(u: Usuario): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update(u: Usuario)

    @Delete
    suspend fun delete(u: Usuario)
}