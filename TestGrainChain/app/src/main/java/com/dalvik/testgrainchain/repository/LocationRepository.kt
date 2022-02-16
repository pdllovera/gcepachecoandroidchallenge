package com.dalvik.testgrainchain.repository

import androidx.lifecycle.LiveData
import com.dalvik.testgrainchain.entities.Locations
import com.dalvik.testgrainchain.room.RoomSingleton
import org.jetbrains.anko.doAsync

class LocationRepository(private var database: RoomSingleton) {

    companion object {
        private var INSTANCE: LocationRepository? = null
        fun getInstance(database: RoomSingleton) = INSTANCE ?: LocationRepository(database).also {
            INSTANCE = it
        }
    }


    fun getAllLocations(): LiveData<List<Locations>> {
        return database.locationDao().allLocations()
    }

    fun getLocationById(
        id: String,
        onResult: (response: Locations) -> Unit
    ) {
        doAsync {
            onResult(database.locationDao().getDetailLocation(id))
        }
    }


    fun deleteLocation(
        id: String
    ) {
        doAsync {
            database.locationDao().deleteLocationById(id)
        }
    }


    fun insertLocation(location: Locations) {
        doAsync {
            database.locationDao().insertLocation(location)
        }

    }

}