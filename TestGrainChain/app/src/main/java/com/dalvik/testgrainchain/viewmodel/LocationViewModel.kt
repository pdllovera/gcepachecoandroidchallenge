package com.dalvik.testgrainchain.viewmodel

import android.annotation.SuppressLint
import android.location.Location
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dalvik.locationmanagement.LocationManagement
import com.dalvik.testgrainchain.entities.Locations
import com.dalvik.testgrainchain.R
import com.dalvik.testgrainchain.Utils.LocationApplication
import com.dalvik.testgrainchain.repository.LocationRepository
import com.dalvik.testgrainchain.room.RoomSingleton
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import java.lang.ref.WeakReference


class LocationViewModel(var activity: WeakReference<AppCompatActivity>) :
    ViewModel() {

    private val db: RoomSingleton = RoomSingleton.getInstance(activity.get()!!)

     val allLocations: LiveData<List<Locations>> =
        LocationRepository.getInstance(db).getAllLocations()



    private val locationManager = activity.get()?.let { LocationManagement.from(it) }

    companion object {
        fun from(activity: AppCompatActivity) = LocationViewModel(WeakReference(activity))
    }

    val msgButton = MutableLiveData(LocationApplication.instance.getString(R.string.start_tracking))

    @SuppressLint("NewApi")
    fun getLocation(
        isTracking: Boolean = false,
        onResult: (response: Location?) -> Unit
    ) {
        if (isTracking) msgButton.postValue(LocationApplication.instance.getString(R.string.stop_tracking))
        locationManager?.messagePermission(LocationApplication.instance.getString(R.string.message_permission_location))
            ?.messageObtainLocation(LocationApplication.instance.getString(R.string.msg_obtain_location_permissions))?.colorProgress(R.color.purple_200)
            ?.isLocationTracking(isTracking)?.getLastLocation {
                onResult(it)
            }

    }


    fun stopTracking() {
        msgButton.postValue(LocationApplication.instance.getString(R.string.start_tracking))
        locationManager?.stopLocationUpdates()
    }

    fun showDialog(
        polyline: List<LatLng>,
        locations: Locations,
        onResult: (response: Boolean) -> Unit
    ) {
        val builder = AlertDialog.Builder(activity.get()!!, R.style.CustomAlertDialog)
            .create()
        val view = activity.get()?.layoutInflater?.inflate(R.layout.custom_layout_dialog, null)
        val button = view?.findViewById<Button>(R.id.btnSave)
        val edtName = view?.findViewById<EditText>(R.id.edtName)
        builder.setView(view)
        button?.setOnClickListener {
            var nameRoute = edtName?.text.toString()
            if (nameRoute.isEmpty()) {
                onResult(false)
                Toast.makeText(activity.get()!!, LocationApplication.instance.getString(R.string.msg_dialog_name_route), Toast.LENGTH_SHORT).show()
            } else {

                onResult(true)
                Toast.makeText(
                    activity.get()!!,
                    LocationApplication.instance.getString(R.string.msg_route_saved),
                    Toast.LENGTH_SHORT
                ).show()

                locations.name = nameRoute
                locations.polyline = Gson().toJson(polyline)
                LocationRepository.getInstance(db).insertLocation(locations)
                builder.dismiss()

            }
        }
        builder.setCanceledOnTouchOutside(false)
        builder.show()
    }


    fun getDetailLocation(id: String, onResult: (response: Locations) -> Unit)  {
        LocationRepository.getInstance(db).getLocationById(id){location ->
            if(location!=null){
                onResult(location)
            }
        }
    }

    fun getDeleteLocation(id: String)  {
        LocationRepository.getInstance(db).deleteLocation(id)
    }


}