package com.misiontic.saber11.database
import android.content.Context
import androidx.room.*
import com.misiontic.saber11.entities.Pregunta
import com.misiontic.saber11.entities.Usuario
import com.misiontic.saber11.dao.PreguntasDAO
import com.misiontic.saber11.dao.UsuarioDAO

@Database(entities = arrayOf(Usuario::class, Pregunta::class), version = 1)
abstract class Saber11Database:RoomDatabase() {
    abstract fun usuarioDao(): UsuarioDAO
    abstract fun preguntaDao(): PreguntasDAO

    companion object{
        @Volatile
        private var INSTANCE: Saber11Database? = null

        fun getDatabase(context: Context): Saber11Database {
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(context.applicationContext,
                    Saber11Database::class.java,
                    "Saber11DB"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}