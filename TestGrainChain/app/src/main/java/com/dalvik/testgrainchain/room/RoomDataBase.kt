package com.dalvik.testgrainchain.room

import android.content.Context
import androidx.room.*
import com.dalvik.testgrainchain.Utils.Constants
import com.dalvik.testgrainchain.entities.Locations

@Database(entities = [Locations::class], version = 1, exportSchema = false)
abstract class RoomSingleton: RoomDatabase() {

    abstract fun locationDao():LocationDao

    companion object{
        private var INSTANCE: RoomSingleton? = null
        fun getInstance(context: Context): RoomSingleton{
            if (INSTANCE == null){
                INSTANCE = Room.databaseBuilder(
                    context,
                    RoomSingleton::class.java,
                    Constants.DATABASE_NAME)
                    .build()
            }

            return INSTANCE as RoomSingleton
        }
    }

}