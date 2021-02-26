package com.example.realestatemanager.view.fragment

import android.Manifest
import android.app.DatePickerDialog
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.utils.toFRDate
import com.example.realestatemanager.view.adapter.RecyclerAdapter
import com.example.realestatemanager.viewModel.FragmentSearchViewModel
import kotlinx.android.synthetic.main.fragment_create_estate.*
import kotlinx.android.synthetic.main.fragment_search_estate.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentSearchEstate : Fragment(), RecyclerAdapter.ItemClickListener {

    private var day = 0
    private var month = 0
    private var year = 0

    private val ESTATE_ID = "ESTATE_ID"
    private val PERMISSION_CODE_READ = 1001
    private val PERMISSION_CODE_WRITE = 1002

    private lateinit var mCheckboxContainer: LinearLayout
    private lateinit var mViewModel: FragmentSearchViewModel

    private lateinit var adapter: RecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var listEstate: ArrayList<EstateEntity>



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_search_estate, container, false)
        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
            .get(FragmentSearchViewModel::class.java)
        recyclerView = view.findViewById(R.id.fragment_estate_search_recyclerview)
        checkPermissionForImage()
        configureRecyclerView()
        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mCheckboxContainer = view.findViewById(R.id.search_checkbox_container)
        pickDate()
        SearchClick()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun getDateTimeCalendar() {
        val cal = Calendar.getInstance()
        day = cal.get(Calendar.DAY_OF_MONTH)
        month = cal.get(Calendar.MONTH)
        year = cal.get(Calendar.YEAR)
    }

    private fun configureRecyclerView() {
        listEstate = ArrayList()
        adapter = RecyclerAdapter(listEstate, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun updateUI(results: List<EstateEntity>) {

        this.listEstate.clear()
        this.listEstate.addAll(results)
        adapter.setResults(results)
        adapter.notifyDataSetChanged()
    }

    private fun pickDate() {

        val sdf = SimpleDateFormat("dd/MM/yyyy")

        search_begin_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, { view, year, monthOfYear, dayOfMonth ->
                search_begin_date.setText(sdf.format(Date(year - 1900, monthOfYear, dayOfMonth)),
                    TextView.BufferType.EDITABLE)
            }, year, month, day).show()

        }

        search_end_date.setOnClickListener {
            getDateTimeCalendar()

            DatePickerDialog(context!!, { view, year, monthOfYear, dayOfMonth ->
                search_end_date.setText(sdf.format(Date(year - 1900, monthOfYear, dayOfMonth)),
                    TextView.BufferType.EDITABLE)
            }, year, month, day).show()
        }

    }

    private fun searchQuery() {
        val beginSearchDate = try {
            search_begin_date.text.toString().toFRDate()
        } catch (e: Exception) {
            null
        }
        val endSearchDate = try {
            search_end_date.text.toString().toFRDate()
        } catch (e: Exception) {
            null
        }
        val pointOfInterest = retrieveSelectedCheckbox().toString()
        val type = search_type.text.toString()
        val priceMin = search_min_price.text.toString().toIntOrNull()
        val priceMax = search_max_price.text.toString().toIntOrNull()
        val state = search_esate_state.text.toString()
        val location = search_estate_location.text.toString()

        var conditions = false

        var query = "SELECT * FROM estateInfo"

        if (type != "") {
            query += " WHERE type = '$type' "
            conditions = true
        }

        if (state == "available" || state == "sold") {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "status = '$state' "
        }

        if (location != ""){
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "city ='$location' "
        }

       if (pointOfInterest != "") {
           query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "pointOfInterest = '$pointOfInterest' "
        }

        if (priceMin != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price >= '$priceMin' "
        }

        if (priceMax != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "price <= '$priceMax' "
        }

        if (beginSearchDate != null) {
            query += if (conditions) " AND " else " WHERE "; conditions = true
            query += "'entry date' >= '$beginSearchDate' "
        }

        if (endSearchDate != null) {
            query += if (conditions) " AND " else " WHERE ";
            query += "'entry date' <= '$endSearchDate' "
        }



        mViewModel.getEstatesBySearch(query)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it!!.isNotEmpty()) {
                    updateUI(it)
                    Log.e("estateSearch", "estateAddress = ${it[0].address}")
                } else {
                    Toast.makeText(this.context!!, "no result found", Toast.LENGTH_SHORT).show()
                }
            })


    }

    private fun SearchClick() {
        search_button.setOnClickListener() {
            searchQuery()
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

    override fun onItemClickListener(estate: EstateEntity) {
        Log.d("TAG", " ${estate.id} " + estate.type + estate.description)
        val bundle = Bundle()
        val fragmentDetailEstate = FragmentDetailEstate()
        val transaction = activity!!.supportFragmentManager.beginTransaction()
        bundle.putInt(ESTATE_ID, estate.id)
        fragmentDetailEstate.arguments = bundle
        transaction.replace(R.id.main_fragment, fragmentDetailEstate).commit()
    }

    private fun checkPermissionForImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if ((ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
                && (ContextCompat.checkSelfPermission(context!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED)
            ) {
                val permission = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
                val permissionCoarse = arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE)

                requestPermissions(permission, PERMISSION_CODE_READ)
                requestPermissions(permissionCoarse, PERMISSION_CODE_WRITE)
            }
        }
    }
}