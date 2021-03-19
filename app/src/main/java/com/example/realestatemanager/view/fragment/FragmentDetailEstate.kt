package com.example.realestatemanager.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.utils.toFRString
import com.example.realestatemanager.view.adapter.RecyclerEstatePhoto
import com.example.realestatemanager.viewModel.FragmentDetailViewModel
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.fragment_detail_estate_final.*
import java.util.ArrayList


class FragmentDetailEstate : Fragment(), OnMapReadyCallback {

    private lateinit var mViewModel: FragmentDetailViewModel
    private val CURRENT_ESTATE_ID = "ESTATE_ID"
    private val apiKey = "AIzaSyDnkdbBMqFIXmCnIcYm0HbU85wRsA35u6c"

    private lateinit var result: EstateEntity
    private var googleMap: GoogleMap? = null

    private lateinit var adapter: RecyclerEstatePhoto
    private lateinit var recyclerView: RecyclerView

    private var lat: Double? = 0.0
    private var lng: Double? = 0.0

    private var imageUriList =ArrayList<String>()


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

         val view: View = inflater.inflate(R.layout.fragment_detail_estate_final, container, false)

        if(arguments != null) {
            val currentEstateId = arguments!!.getInt("ESTATE_ID")
            recyclerView = view.findViewById(R.id.fragment_estate_list_image_recyclerview)
            mViewModel =
                ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(
                    FragmentDetailViewModel::class.java
                )
            this.mViewModel.init(currentEstateId)
            getCurrentEstate(currentEstateId)
            configureRecyclerView()
        }


        return view

    }

    private fun getCurrentEstate(estateId: Int) {
        this.mViewModel.getEstate(estateId).observe(viewLifecycleOwner, Observer { updateUi(it) })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.toolbar_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun updateUi(result: EstateEntity) {


        detail_description.text = result.description
        surface_detail.text = result.surface.toString()
        nb_room_detail.text = result.nbPiece
        nb_bathroom_detail.text = result.nbBathroom
        nb_bedroom_detail.text = result.nbBedroom
        address_detail.text = result.address
        detail_city.text = result.city
        detail_price.text = result.price.toString()
        agent_detail.text = result.agentName
        status_detail.text = result.status
        beginDate_detail.text = result.entryDate.toFRString()
        endDate_detail.text = result.dateOfSale?.toFRString()

        this.result = result
        val address = result.address + " " + result.city
        Log.d("TAG", "address : $address ")

        mViewModel.getGeocodedLocation(address, apiKey)

        val mapFragment =
            childFragmentManager.findFragmentById(R.id.detail_fragment_map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        var uriString = result.photo
        var poI = result.pointOfInterest

        if (poI != null) {
            poI = poI.replace("[", "")
            poI = poI.replace("]", "")
            val listPoI = poI.split(", ")
            Log.d("TAG", "listpoI : $listPoI ")

            listPoI.forEach {
                if (it == "école"){
                    image_school.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                }else{
                    image_school.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_close_24,null))
                }

                if (it == "commerce"){
                    shop_image.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                }else{
                    shop_image.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_close_24,null))
                }

                if (it == "parc"){
                    image_park.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                }else{
                    image_park.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_close_24,null))
                }

                if (it == "gare"){
                    station_image.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_check_24,null))
                }else{
                    station_image.setImageDrawable(ResourcesCompat.getDrawable(resources,R.drawable.ic_baseline_close_24,null))
                }
            }
        }

        if (uriString != null) {
            uriString = uriString.replace("[", "")
            uriString = uriString.replace("]", "")
            val listUriImage = uriString.split(", ")
            Log.d("TAG", "listUriImage : $listUriImage ")

            listUriImage.forEach {
                imageUriList.add(it)
                Log.d("TAG", "imageUriList : $imageUriList ")

            }

            adapter.setResults(imageUriList)
            Log.d("TAG", "fullImageUriList : $imageUriList ")
            adapter.notifyDataSetChanged()


        }

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val currentEstateId = arguments!!.getInt("ESTATE_ID")
        val tabletFrag = activity!!.findViewById<FrameLayout>(R.id.list_detail_fragment)


        if (id == R.id.edit_estate) {
            Toast.makeText(context, "search icon clicked ", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            val createEstateFragment = CreateEstateFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            bundle.putInt(CURRENT_ESTATE_ID, currentEstateId)
            createEstateFragment.arguments = bundle
            if (tabletFrag != null) {
                transaction.replace(R.id.list_detail_fragment, createEstateFragment).commit()
            }else{
                transaction.replace(R.id.main_fragment, createEstateFragment).commit()

            }
        }

        return super.onOptionsItemSelected(item)

    }

    private fun retrieveAddressLocation(googleMap: GoogleMap) {
        mViewModel.response.observe(viewLifecycleOwner, Observer {
            if (it != null)
                Toast.makeText(context, "Success wile accessing the API", Toast.LENGTH_SHORT).show()

            lat = it.geometry?.location?.lat
            lng = it.geometry?.location?.lng
            if (lat != null && lng != null) {
                val latLng = LatLng(lat!!, lng!!)
                val markerOptions: MarkerOptions =
                    MarkerOptions().position(latLng).title("Current Estate")
                val zoomLevel = 17.0f //This goes up to 21
                googleMap.addMarker(markerOptions)
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoomLevel))
            }
            Log.d("TAG", "location : $lat $lng")
            Log.d("TAG", "result : $it")


        })
    }

    override fun onMapReady(p0: GoogleMap?) {
        this.googleMap = p0
        //Adding markers to map

        googleMap?.let {

            retrieveAddressLocation(it)

        }
    }

    private fun configureRecyclerView() {
        adapter = RecyclerEstatePhoto(imageUriList)
        val mLayoutManager = LinearLayoutManager(activity)
        mLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        recyclerView.layoutManager = mLayoutManager
        recyclerView.adapter = adapter
    }


}