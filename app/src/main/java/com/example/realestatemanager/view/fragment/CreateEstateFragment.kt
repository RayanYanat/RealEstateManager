package com.example.realestatemanager.view.fragment

import android.Manifest
import android.app.Activity.RESULT_OK
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.utils.toFRDate
import com.example.realestatemanager.utils.toFRString
import com.example.realestatemanager.viewModel.FragmentCreateViewModel
import kotlinx.android.synthetic.main.fragment_create_estate_final.*
import java.text.SimpleDateFormat
import java.util.*


class CreateEstateFragment : Fragment() {

    private lateinit var mViewModel: FragmentCreateViewModel
    private lateinit var button: Button
    private lateinit var mCheckboxContainer: LinearLayout
    private val RESULT_LOAD_IMG = 10

    private val PERMISSION_CODE_READ = 1001
    private val PERMISSION_CODE_WRITE = 1002
    private val CAMERA_PIC_REQUEST = 1111


    private var day = 0
    private var month = 0
    private var year = 0

    private var photoList = ArrayList<String>()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_create_estate_final, container, false)
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
        add_picture_btn.setOnClickListener {
            checkPermissionForImage()
        }

        add_picture_btn_camera.setOnClickListener{
            retrieveImageWithCamera()
        }

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
                val dateOfSale = create_end_date.text.toString()
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
                    if (dateOfSale != ""){
                                         dateOfSale.toFRDate()
                                         }else{
                                              null
                                              },
                    agent,
                    city,
                    photoList.toString())
                mViewModel.createEstate(estate)
                Toast.makeText(context, "estate created ", Toast.LENGTH_SHORT).show()

            }
        } else {
            this.mViewModel.init(currentEstateId)
            getCurrentEstate(currentEstateId)
            button.text = "Update"
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
                val dateOfSale = create_end_date.text.toString()
                val pointOfInterest = retrieveSelectedCheckbox().toString()

                Log.d("TAG", "UpdateInsertListImageURI : $photoList ")


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
                    if (dateOfSale != ""){
                        dateOfSale.toFRDate()
                    }else{
                        null
                    },
                    agent,
                    city,
                    photoList.toString()
                )
                estate.id = currentEstateId
                mViewModel.updateEstate(estate)
                Toast.makeText(context, "estate updated ", Toast.LENGTH_SHORT).show()
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
        create_begin_date.setText(result.entryDate.toFRString())
        if (result.dateOfSale.toString() != ""){
            create_end_date.setText(result.dateOfSale?.toFRString())
        }


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

    //retrieve image from gallery
    fun retrieveImage() {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            intent.type = "image/*"
             intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(Intent.createChooser(intent, "select a picture"),
                RESULT_LOAD_IMG)

    }

    //retrieve image from camera
    fun retrieveImageWithCamera(){
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, CAMERA_PIC_REQUEST);
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RESULT_LOAD_IMG) {
            if (resultCode == RESULT_OK) {
                if (data?.data != null) {
                    val selectedImageUri: Uri? = data.data

                        photoList.add(selectedImageUri.toString())
                        Log.d("TAG", "geURI : ${selectedImageUri} ")

                } else if (data!!.clipData != null) {
                    (0 until data.clipData!!.itemCount).forEach { i ->
                        val uri = data.clipData!!.getItemAt(i).uri
                        photoList.add(uri.toString())
                    }
                }
                Log.d("TAG", "ListImageURI : $photoList ")

            }
        }else if (resultCode == CAMERA_PIC_REQUEST){
            if(resultCode == RESULT_OK){
                if (data?.data != null){
                    val selectedImageUri: Uri? = data.data
                    photoList.add(selectedImageUri.toString())
                }
            }

        }
    }

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((checkSelfPermission(context!!, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (checkSelfPermission(context!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ)
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE)
            } else {
                retrieveImage()
            }
        }
    }
    }





