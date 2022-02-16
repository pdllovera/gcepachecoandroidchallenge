package com.dalvik.testgrainchain.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.dalvik.testgrainchain.entities.Locations
import com.dalvik.testgrainchain.R
import com.dalvik.testgrainchain.Utils.Constants
import com.dalvik.testgrainchain.databinding.ActivityListBinding
import com.dalvik.testgrainchain.viewmodel.LocationViewModel

class ListActivity : AppCompatActivity() {

    private lateinit var binding : ActivityListBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var adapter : ArrayAdapter<Locations>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_list)

        initConfig()

        binding.lvLocations.setOnItemClickListener { parent, view, position, id ->
            val element = adapter.getItem(position) // The item that was clicked
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra(Constants.ID_LOCATION, element!!.id)
            }
            startActivity(intent)
        }
    }

    private fun initConfig() {
        locationViewModel = LocationViewModel.from(this)

        locationViewModel.allLocations.observe(this, Observer { locations ->
            adapter = ArrayAdapter(this, R.layout.item_list_location, locations)
            binding.lvLocations.adapter = adapter
        })


    }

}