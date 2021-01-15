package com.example.realestatemanager.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.DatePicker
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.viewModel.FragmentCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_estate.*
import java.text.SimpleDateFormat
import java.util.*

class CreateEstateFragment : Fragment(), DatePickerDialog.OnDateSetListener {

    private lateinit var mViewModel: FragmentCreateViewModel
    private lateinit var button: Button
    private lateinit var mCheckboxContainer: LinearLayout

    private var day = 0
    private var month = 0
    private var year = 0

    private var savedDay = 0
    private var savedMonth = 0
    private var savedYear = 0


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_estate, container, false)
        mViewModel =
            ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(
                FragmentCreateViewModel::class.java
            )
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        button = view.findViewById(R.id.create_estate_button)
        mCheckboxContainer = view.findViewById(R.id.checkbox_container)

        val currentEstateId = arguments?.getInt("ESTATE_ID")

        pickDate()

        if (currentEstateId == null) {
            button.setOnClickListener {

                val type = estate_type.text.toString()
                val city = estate_city.text.toString()
                val price = estate_price.text.toString()
                val surface = estate_surface.text.toString()
                val nbOfRoom = estate_nb_room.text.toString()
                val nbOfBathroom = estate_nb_bathroom.text.toString()
                val nbOfBedroom = estate_nb_bedroom.text.toString()
                val description = estate_description.text.toString()
                val address = estate_adress.text.toString()
                val statut = estate_statut.text.toString()
                val agent = estate_agent.text.toString()
                val entryDate = create_begin_date.text.toString()
                val pointOfInterest = retrieveSelectedCheckbox().toString()


                val estate = EstateEntity(
                    type,
                    price,
                    surface,
                    nbOfRoom,
                    nbOfBathroom,
                    nbOfBedroom,
                    description,
                    address,
                    pointOfInterest,
                    statut,
                    entryDate,
                    null,
                    agent,
                    city,
                    null
                )
                mViewModel.createEstate(estate)
            }
        } else {
            this.mViewModel.init(currentEstateId)
            getCurrentEstate(currentEstateId)
            button.setText("Update")
            button.setOnClickListener {

                val type = estate_type.text.toString()
                val city = estate_city.text.toString()
                val price = estate_price.text.toString()
                val surface = estate_surface.text.toString()
                val nbOfRoom = estate_nb_room.text.toString()
                val nbOfBathroom = estate_nb_bathroom.text.toString()
                val nbOfBedroom = estate_nb_bedroom.text.toString()
                val description = estate_description.text.toString()
                val address = estate_adress.text.toString()
                val statut = estate_statut.text.toString()
                val agent = estate_agent.text.toString()
                val entryDate = create_begin_date.text.toString()
                val pointOfInterest = retrieveSelectedCheckbox().toString()


                val estate = EstateEntity(
                    type,
                    price,
                    surface,
                    nbOfRoom,
                    nbOfBathroom,
                    nbOfBedroom,
                    description,
                    address,
                    pointOfInterest,
                    statut,
                    entryDate,
                    null,
                    agent,
                    city,
                    null
                )
                estate.id = currentEstateId
                mViewModel.updateEstate(estate)
            }

        }
    }

    private fun updateUi(result: EstateEntity) {
        estate_type.setText(result.type)
        estate_city.setText(result.city)
        estate_price.setText(result.price)
        estate_surface.setText(result.surface)
        estate_nb_room.setText(result.nbPiece)
        estate_nb_bathroom.setText(result.nbBathroom)
        estate_nb_bedroom.setText(result.nbBedroom)
        estate_description.setText(result.description)
        estate_adress.setText(result.address)
        estate_statut.setText(result.status)
        estate_agent.setText(result.agentName)
        create_begin_date.setText(result.entryDate)
    }


    private fun getCurrentEstate(estateId: Int?) {
        this.mViewModel.getEstate(estateId).observe(viewLifecycleOwner, Observer { updateUi(it) })
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun pickDate() {


        create_begin_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, this, year, month, day).show()

        }

        create_end_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, this, year, month, day).show()
        }
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {

        val sdf = SimpleDateFormat("dd/MM/yyyy")


        savedDay = dayOfMonth
        savedMonth = month
        savedYear = year

        // var dateText = "$savedDay - $savedMonth - $savedYear"
        getDateTimeCalendar()

        create_begin_date.setText(sdf.format(Date(savedYear - 1900, savedMonth, savedDay)))
    }

    private fun retrieveSelectedCheckbox(): List<String>? {
        val selectedCheckboxes: MutableList<String> = ArrayList()
        for (i in 0 until mCheckboxContainer.childCount) {
            val view = mCheckboxContainer.getChildAt(i)
            if (view is ViewGroup) {
                val viewGroup = view
                for (y in 0 until viewGroup.childCount) {
                    val viewChild = viewGroup.getChildAt(y)
                    if (viewChild is CheckBox) {
                        val checkBox = viewChild
                        if (checkBox.isChecked) {
                            selectedCheckboxes.add(checkBox.tag.toString())
                        }
                    }
                }
            }
        }
        return selectedCheckboxes
    }

}