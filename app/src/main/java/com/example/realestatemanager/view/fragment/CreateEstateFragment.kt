package com.example.realestatemanager.view.fragment

import android.R.attr
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.utils.toFRDate
import com.example.realestatemanager.utils.toFRString
import com.example.realestatemanager.viewModel.FragmentCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_estate.*
import java.text.SimpleDateFormat
import java.util.*


class CreateEstateFragment : Fragment() {

    private lateinit var mViewModel: FragmentCreateViewModel
    private lateinit var button: Button
    private lateinit var mCheckboxContainer: LinearLayout
    private val RESULT_LOAD_IMG = 10


    private var day = 0
    private var month = 0
    private var year = 0

    private var photoList = ArrayList<String>()


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
        retrieveImage()

        if (currentEstateId == null) {
            button.setOnClickListener {

                val type = estate_type.text.toString()
                val city = estate_city.text.toString()
                val price = estate_price.text.toString().toInt()
                val surface = estate_surface.text.toString().toInt()
                val nbOfRoom = estate_nb_room.text.toString()
                val nbOfBathroom = estate_nb_bathroom.text.toString()
                val nbOfBedroom = estate_nb_bedroom.text.toString()
                val description = estate_description.text.toString()
                val address = estate_adress.text.toString()
                val statut = estate_statut.text.toString()
                val agent = estate_agent.text.toString()
                val entryDate = create_begin_date.text.toString().toFRDate()
             //   val dateOfSale = create_end_date.text.toString().toFRDate()
                val pointOfInterest = retrieveSelectedCheckbox().toString()

                Log.d("TAG", "InsertListImageURI : $photoList ")

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
                    entryDate!!,
                    null,
                    agent,
                    city,
                    photoList.toString())
                mViewModel.createEstate(estate)
            }
        } else {
            this.mViewModel.init(currentEstateId)
            getCurrentEstate(currentEstateId)
            button.setText("Update")
            button.setOnClickListener {

                val type = estate_type.text.toString()
                val city = estate_city.text.toString()
                val price = estate_price.text.toString().toInt()
                val surface = estate_surface.text.toString().toInt()
                val nbOfRoom = estate_nb_room.text.toString()
                val nbOfBathroom = estate_nb_bathroom.text.toString()
                val nbOfBedroom = estate_nb_bedroom.text.toString()
                val description = estate_description.text.toString()
                val address = estate_adress.text.toString()
                val statut = estate_statut.text.toString()
                val agent = estate_agent.text.toString()
                val entryDate = create_begin_date.text.toString().toFRDate()
              //  val dateOfSale = create_end_date.text.toString().toFRDate()
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
                    entryDate!!,
                    null,
                    agent,
                    city,
                    photoList.toString()
                )
                estate.id = currentEstateId
                mViewModel.updateEstate(estate)
            }

        }
    }

    private fun updateUi(result: EstateEntity) {

        estate_type.setText(result.type)
        estate_city.setText(result.city)
        estate_price.setText(result.price.toString())
        estate_surface.setText(result.surface.toString())
        estate_nb_room.setText(result.nbPiece)
        estate_nb_bathroom.setText(result.nbBathroom)
        estate_nb_bedroom.setText(result.nbBedroom)
        estate_description.setText(result.description)
        estate_adress.setText(result.address)
        estate_statut.setText(result.status)
        estate_agent.setText(result.agentName)
        create_begin_date.setText(result.entryDate?.toFRString())


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

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        create_begin_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, { view, year, monthOfYear, dayOfMonth ->
                create_begin_date.setText(sdf.format(Date(year - 1900, monthOfYear, dayOfMonth)),
                    TextView.BufferType.EDITABLE)
            }, year, month, day).show()

        }

        create_end_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, { view, year, monthOfYear, dayOfMonth ->
                create_end_date.setText(sdf.format(Date(year - 1900, monthOfYear, dayOfMonth)),
                    TextView.BufferType.EDITABLE)
            }, year, month, day).show()
        }
    }

    private fun retrieveSelectedCheckbox(): List<String>? {
        val selectedCheckboxes: MutableList<String> = ArrayList()
        for (i in 0 until mCheckboxContainer.childCount) {
            val view = mCheckboxContainer.getChildAt(i)
            if (view is ViewGroup) {
                for (y in 0 until view.childCount) {
                    val viewChild = view.getChildAt(y)
                    if (viewChild is CheckBox) {
                        if (viewChild.isChecked) {
                            selectedCheckboxes.add(viewChild.tag.toString())
                        }
                    }
                }
            }
        }
        return selectedCheckboxes
    }

    fun retrieveImage() {
        add_picture_btn.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_OPEN_DOCUMENT;
            startActivityForResult(Intent.createChooser(intent, "select a picture"),
                RESULT_LOAD_IMG);
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                val selectedImageUri: Uri? = data?.data
                photoList.add(selectedImageUri.toString())
                Log.d("TAG", "imageURI : ${selectedImageUri.toString()} ")
                Log.d("TAG", "ListImageURI : $photoList ")

            }
        }
    }
}