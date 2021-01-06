package com.example.realestatemanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.viewModel.FragmentCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_estate.*

class CreateEstateFragment: Fragment() {

    private lateinit var mViewModel: FragmentCreateViewModel
    private lateinit var button: Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view : View = inflater.inflate(R.layout.fragment_create_estate, container, false)
        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(FragmentCreateViewModel::class.java)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.create_estate_button)
        button.setOnClickListener(){
            var type = estate_type.text.toString()
            var city = estate_city.text.toString()
            var price = estate_price.text.toString()
            var surface = estate_surface.text.toString()
            var nbOfRoom = estate_nb_room.text.toString()
            var nbOfBathroom = estate_nb_bathroom.text.toString()
            var nbOfBedroom = estate_nb_bedroom.text.toString()
            var description = estate_description.text.toString()
            var address = estate_adress.text.toString()
            var statut = estate_statut.text.toString()
            var agent = estate_agent.text.toString()

            var estate = EstateEntity (type,price,surface,nbOfRoom,nbOfBathroom,nbOfBedroom,description,address,null,statut,"01/02/2020",null,agent,city,null)
            mViewModel.createEstate(estate)
        }
    }

}