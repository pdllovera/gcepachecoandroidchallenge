package com.dalvik.testgrainchain.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.dalvik.testgrainchain.R
import com.dalvik.testgrainchain.Utils.Constants
import com.dalvik.testgrainchain.databinding.ActivityDetailBinding
import com.dalvik.testgrainchain.entities.Locations
import com.dalvik.testgrainchain.viewmodel.LocationViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var mMap: GoogleMap
    private  var locationSelected: Locations = Locations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_detail)

        initConfig()
        getElementsExtra()

        binding.btnDeleteLocation.setOnClickListener {
            locationViewModel.getDeleteLocation(locationSelected.id)
            Toast.makeText(this,getString(R.string.msg_delete_location),Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun initConfig() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.mapDetail) as SupportMapFragment
        mapFragment.getMapAsync(this)

        locationViewModel = LocationViewModel.from(this)
    }

    private fun getElementsExtra() {
        val message = intent.getStringExtra(Constants.ID_LOCATION)
       locationViewModel.getDetailLocation(message!!){ location ->
           if(supportActionBar!=null){
               locationSelected = location
               binding.location = location
               supportActionBar?.title = location.name
           }
        }
    }

    override fun onMapReady(map: GoogleMap) {
        mMap = map
        setInformationMap()
     }

    private fun setInformationMap(){
        val measurements : List<LatLng> = Gson().fromJson(locationSelected.polyline, object : TypeToken<List<LatLng>>() {}.type)
        val polyline = PolylineOptions()
        polyline.addAll(measurements)
        mMap.addPolyline(polyline)

        //Posible metodo para centrar entre dos ubicaciones

       /*val endLatLng = LatLng(locationSelected.latitudEnd.toDouble(), locationSelected.longitudEnd.toDouble())
        val startLatLng = LatLng(locationSelected.latitudStart.toDouble(), locationSelected.longitudStart.toDouble())
        val latLngBounds = LatLngBounds(startLatLng, endLatLng)*/
        val startLatLng = LatLng(locationSelected.latitudStart.toDouble(), locationSelected.longitudStart.toDouble())
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startLatLng, Constants.ZOOM_MAP))
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_share -> {
            val intent= Intent()
            intent.action=Intent.ACTION_SEND
            intent.putExtra(Intent.EXTRA_TEXT,"Te comparto mi recorrido distancia: ${locationSelected.getDistanceConverter()} y tiempo ${locationSelected.getFormatEndTime()}")
            intent.type="text/plain"
            startActivity(Intent.createChooser(intent,""))
            true
        }
        else -> {
            super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.detail, menu)
        return true
    }
}