package com.dalvik.testgrainchain.entities

import android.location.Location
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dalvik.testgrainchain.Utils.Methods

@Entity(tableName = "locationTbl")
data class Locations(
    @PrimaryKey
    var id: String = "",

    @ColumnInfo(name = "name")
    var name: String = "",

    @ColumnInfo(name = "date")
    var date: String = "",

    @ColumnInfo(name = "polyline")
    var polyline: String = "",

    @ColumnInfo(name = "latitudStart")
    var latitudStart: String = "",

    @ColumnInfo(name = "longitudStart")
    var longitudStart: String = "",

    @ColumnInfo(name = "latitudEnd")
    var latitudEnd: String = "",

    @ColumnInfo(name = "longitudEnd")
    var longitudEnd: String = "",

    @ColumnInfo(name = "timeStart")
    var timeStart: String = "",

    @ColumnInfo(name = "timeEnd")
    var timeEnd: String = ""
) {
    override fun toString(): String {
        return "${getDistanceConverter()} · $name"
    }

    fun distance(): Float {
        val locationStart = Location("")
        val locationEnd = Location("")
        if (latitudEnd.isNotEmpty() && longitudEnd.isNotEmpty()) {
            locationStart.latitude = latitudStart.toDouble()
            locationStart.longitude = longitudStart.toDouble()
            locationEnd.latitude = latitudEnd.toDouble()
            locationEnd.longitude = longitudEnd.toDouble()
        }
        return (locationStart.distanceTo(locationEnd) / 1000)
    }

    fun getDistanceConverter(): String {
        return "${String.format("%.3f", distance()).toDouble()} km"
    }

    fun formatTimeStart(): String {
        return Methods.getTimeFormat(timeStart)
    }

    fun formatTimeEnd(): String {
        return Methods.getTimeFormat(timeEnd)
    }

    fun getFormatEndTime(): String {
        return "${Methods.getTimeDiff(timeStart, timeEnd)} min"
    }
}

