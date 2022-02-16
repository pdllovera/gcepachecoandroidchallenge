package com.dalvik.testgrainchain.view

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dalvik.locationmanagement.LocationManagement
import com.dalvik.testgrainchain.entities.Locations
import com.dalvik.testgrainchain.R
import com.dalvik.testgrainchain.Utils.Constants
import com.dalvik.testgrainchain.databinding.MapsActivityBinding
import com.dalvik.testgrainchain.viewmodel.LocationViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import kotlin.random.Random


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    //Declaracion de variables
    private lateinit var mMap: GoogleMap
    private lateinit var binding: MapsActivityBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var polyline: PolylineOptions
    private lateinit var locationManager: LocationManagement
    private lateinit var locations: Locations

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //Configuracion de la vista
        binding = DataBindingUtil.setContentView(this, R.layout.maps_activity)
        val options = GoogleMapOptions().liteMode(true)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //Llamamos al metodo de la configuracion inicial
        initConfig()

        //Clic del boton para iniciar el tracking
        binding.btnStartracking.setOnClickListener {
            startTracking()
        }



    }

    override fun onResume() {
        super.onResume()

        //Obtenemos ubicacion cuando se carga la vista
        getLocation()

    }


    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_list -> {
            startActivity(Intent(this, ListActivity::class.java))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.principal, menu)
        return true
    }

    private fun initConfig() {
        locationViewModel = LocationViewModel.from(this)
        locationManager = LocationManagement.from(this)
        binding.viewModel = locationViewModel
        binding.lifecycleOwner = this
        polyline = PolylineOptions()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

    }

    @SuppressLint("NewApi")
    fun startTracking() {
        if (binding.btnStartracking.text.equals(getString(R.string.start_tracking))) {
            locations = Locations()
            var mark = true
            locationViewModel.getLocation(true) { it ->
                if (it != null) {
                    if (mark) {
                        mMap.addMarker(
                            MarkerOptions().position(LatLng(it.latitude, it.longitude))
                                .title(getString(R.string.start_tracking_route))
                        )
                        mark = false
                        locations.id = Random.nextInt().toString()
                        locations.latitudStart = it.latitude.toString()
                        locations.longitudStart = it.longitude.toString()
                        locations.timeStart = System.currentTimeMillis().toString()
                    }
                    polyline.add(LatLng(it!!.latitude, it!!.longitude))
                    mMap.addPolyline(polyline)
                }
            }
        } else {
            getLocation(getString(R.string.end_tracking_route))
            locationViewModel.showDialog(polyline.points, locations) { saveData ->
                if (saveData) {
                    mMap.clear()
                    polyline = PolylineOptions()
                    locationViewModel.stopTracking()
                }
            }
        }
    }


    @SuppressLint("MissingPermission")
    private fun getLocation(message: String = "") {
        locationViewModel.getLocation(false) { location ->
            if (location != null) {
                mMap.isMyLocationEnabled = true
                val currentLocation = LatLng(location.latitude, location.longitude)
                if (message.isNotEmpty()) {
                    mMap.addMarker(
                        MarkerOptions().position(currentLocation)
                            .title(message)
                    )
                }
                if(message.equals(getString(R.string.end_tracking_route))){
                    locations.longitudEnd = location.longitude.toString()
                    locations.latitudEnd = location.latitude.toString()
                    locations.timeEnd = System.currentTimeMillis().toString()
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, Constants.ZOOM_MAP))
            }

        }
    }

}