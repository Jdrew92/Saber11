package com.misiontic.proyecto.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.misiontic.proyecto.Entities.Pregunta
import com.misiontic.proyecto.dao.PreguntasDAO

@Database ( entities = [Pregunta::class], version = 1 )
abstract class PreguntasDB : RoomDatabase() {
    //Operaciones de la BD
    abstract fun preguntasDAO() : PreguntasDAO

    //Instancia BD
    companion object{
        @Volatile
        private var INSTANCE : PreguntasDB? = null

        fun getDataBase(context: Context ) : PreguntasDB{
            val tempInstance = INSTANCE

            if ( tempInstance != null){
                return tempInstance
            }
            synchronized( this ){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PreguntasDB::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}