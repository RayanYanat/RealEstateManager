package com.example.realestatemanager.view.fragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.viewModel.FragmentDetailViewModel
import kotlinx.android.synthetic.main.fragment_detail_estate.*

class FragmentDetailEstate : Fragment() {

    private lateinit var mViewModel: FragmentDetailViewModel
    private val CURRENT_ESTATE_ID = "ESTATE_ID"
    private val apiKey = "AIzaSyDnkdbBMqFIXmCnIcYm0HbU85wRsA35u6c"

    private lateinit var result: EstateEntity


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val currentEstateId = arguments!!.getInt("ESTATE_ID")
        mViewModel =
            ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(
                FragmentDetailViewModel::class.java
            )
        this.mViewModel.init(currentEstateId)
        getCurrentEstate(currentEstateId)

        return inflater.inflate(R.layout.fragment_detail_estate, container, false)

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
        surface_detail.text = result.surface
        nb_room_detail.text = result.nbPiece
        nb_bathroom_detail.text = result.nbBathroom
        nb_bedroom_detail.text = result.nbBedroom
        address_detail.text = result.address
        detail_type.text = result.type
        detail_city.text = result.city
        detail_price.text = result.price
        agent_detail.text = result.agentName
        status_detail.text = result.status
        beginDate_detail.text = result.entryDate
        endDate_detail.text = result.dateOfSale
        checkbox_detail.text = result.pointOfInterest.toString()

        this.result = result
        val address = result.address + " " + result.city
        Log.d("TAG", "address : $address ")

        mViewModel.getGeocodedLoccation(address,apiKey)

        retrieveAddressLocation()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        val currentEstateId = arguments!!.getInt("ESTATE_ID")


        if (id == R.id.edit_estate) {
            Toast.makeText(context, "search icon clicked ", Toast.LENGTH_SHORT).show()
            val bundle = Bundle()
            val createEstateFragment = CreateEstateFragment()
            val transaction = activity!!.supportFragmentManager.beginTransaction()
            bundle.putInt(CURRENT_ESTATE_ID, currentEstateId)
            createEstateFragment.arguments = bundle
            transaction.replace(R.id.main_fragment, createEstateFragment).commit()
        }

        return super.onOptionsItemSelected(item)

    }

   private  fun retrieveAddressLocation() {
        mViewModel.response.observe(viewLifecycleOwner, Observer {
            if (it != null)
                Toast.makeText(context,"Success wile accessing the API", Toast.LENGTH_SHORT).show()

            val lat  = it.geometry?.location?.lat
            val lng = it.geometry?.location?.lng

            Log.d("TAG", "location : $lat$lng")


        })
    }


}