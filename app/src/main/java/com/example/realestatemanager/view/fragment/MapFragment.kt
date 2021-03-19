package com.example.realestatemanager.view.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.viewModel.FragmentMapViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var googleMap: GoogleMap? = null
    private val apiKey = "AIzaSyDnkdbBMqFIXmCnIcYm0HbU85wRsA35u6c"
    private val ESTATE_ID = "ESTATE_ID"
    private lateinit var mViewModel: FragmentMapViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lat: Double? = 0.0
    private var lng: Double? = 0.0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_map, container, false)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        mViewModel =
            ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(
                FragmentMapViewModel::class.java
            )
        return view
    }


    override fun onMapReady(p0: GoogleMap?) {
        googleMap = p0

        //Adding markers to map


        //retrieve the current user position
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context!!)
        if (ActivityCompat.checkSelfPermission(context!!,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    val currentPosition = LatLng(location.latitude, location.longitude)
                    Log.d("TAG", "OnMapReady = location $location")
                    val markerOptions: MarkerOptions = MarkerOptions().position(currentPosition).title("your position")
                    // moving camera and zoom map
                    googleMap.let {
                        it!!.addMarker(markerOptions)
                        it.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 14F))
                    }
                }
            }
        }else {
            val latLng = LatLng(48.789664, 2.041166)
            val markerOptions: MarkerOptions =
                MarkerOptions().position(latLng).title("your position")
            // moving camera and zoom map
            googleMap.let {
                it!!.addMarker(markerOptions)
                it.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14F))
            }
        }

        mViewModel.getAllEstate()
            .observe(viewLifecycleOwner, Observer<List<EstateEntity>> { displayNearbyEstate(it!!)  })

    }

    private fun displayNearbyEstate(estateResults: List<EstateEntity>) {

        Log.d("TAG", "estateResults : $estateResults")

        (estateResults.indices).forEach {
            val address = estateResults[it].address
            Log.d("TAG", "estateAddress : $address")
            retrieveAddressLocation(estateResults[it])

        }

        googleMap?.setOnInfoWindowClickListener {
            val marker = it
            (estateResults.indices).forEach { it1 ->
                if (marker.tag.toString() == estateResults[it1].id.toString()) {
                    val bundle = Bundle()
                    val fragmentDetailEstate = FragmentDetailEstate()
                    val transaction = activity!!.supportFragmentManager.beginTransaction()
                    bundle.putInt(ESTATE_ID, estateResults[it1].id)
                    fragmentDetailEstate.arguments = bundle
                    transaction.replace(R.id.main_fragment, fragmentDetailEstate).commit()
                }
            }
        }
    }

    private fun retrieveAddressLocation(estate: EstateEntity) {
        mViewModel.getGeocodedLocation(estate.address, apiKey).observe(viewLifecycleOwner, Observer {
            if (it != null)
                Toast.makeText(context, "Success wile accessing the API", Toast.LENGTH_SHORT).show()

            lat = it.geometry?.location?.lat
            lng = it.geometry?.location?.lng

            Log.d("TAG", "location : $lat $lng")
            Log.d("TAG", "result : $it")

            if (lat != null && lng != null) {
                val estatePosition = LatLng(lat!!, lng!!)
                val markerOptions = MarkerOptions()
                markerOptions.position(estatePosition)
                markerOptions.title(estate.address)
                Log.d("TAG", "estateAddress : ${estate.address}")

                val marker: Marker = googleMap!!.addMarker(markerOptions)
                marker.tag = estate.id
                Log.d("TAG", "estatePosition : $estatePosition")

            } 
        })
    }

}
 

