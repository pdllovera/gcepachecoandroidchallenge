package com.dalvik.testgrainchain.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dalvik.testgrainchain.entities.Locations

@Dao
interface LocationDao {

    @Query("SELECT * FROM locationTbl ORDER BY id DESC")
    fun allLocations(): LiveData<List<Locations>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(locations: Locations)

    @Query("SELECT * FROM locationTbl WHERE id = :id")
    fun getDetailLocation(id: String): Locations


    @Query("DELETE  FROM locationTbl WHERE id = :id")
    fun deleteLocationById(id: String)

}