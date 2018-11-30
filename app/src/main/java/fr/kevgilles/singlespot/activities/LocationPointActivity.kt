package fr.kevgilles.singlespot.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.kevgilles.singlespot.Constants
import fr.kevgilles.singlespot.R
import fr.kevgilles.singlespot.adapter.LocationPointAdapter
import fr.kevgilles.singlespot.data.LocationPoint
import fr.kevgilles.singlespot.viewmodel.LocationPointViewModel

class LocationPointActivity : AppCompatActivity() {

    private lateinit var mLocationViewModel: LocationPointViewModel
    private lateinit var mAdapter: LocationPointAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_point)

        val areaName = intent.getStringExtra(Constants.AREA_NAME)
        mLocationViewModel = ViewModelProviders.of(this).get(LocationPointViewModel::class.java)
        mLocationViewModel.getLocations().observe(this, Observer { refreshRecyclerView(it!!) })
        mLocationViewModel.refreshLocationPoints(areaName)
    }

    private fun refreshRecyclerView(it: List<LocationPoint>) {
        mAdapter = LocationPointAdapter(it)
        val recyclerView = findViewById<RecyclerView>(R.id.rv_location_points)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = mAdapter
    }

}
