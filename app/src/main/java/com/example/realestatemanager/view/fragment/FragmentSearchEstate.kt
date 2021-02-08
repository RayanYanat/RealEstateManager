package com.example.realestatemanager.view.fragment

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.realestatemanager.R
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.utils.toFRDate
import com.example.realestatemanager.viewModel.FragmentSearchViewModel
import kotlinx.android.synthetic.main.fragment_create_estate.*
import kotlinx.android.synthetic.main.fragment_search_estate.*
import java.text.SimpleDateFormat
import java.util.*

class FragmentSearchEstate : Fragment() {

    private var day = 0
    private var month = 0
    private var year = 0


    private lateinit var mCheckboxContainer: LinearLayout
    private lateinit var mViewModel: FragmentSearchViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(context!!))
            .get(FragmentSearchViewModel::class.java)
        return inflater.inflate(R.layout.fragment_search_estate, container, false)
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

        var query = "SELECT * FROM estateInfo"

        if (type != "") {
            query += " WHERE type = '$type'"
        }

        if (state == "available" || state == "sold") {
            query += "WHERE status = '$state'"
        }

//        if (pointOfInterest != "") {
//            query += " AND pointOfInterest = $pointOfInterest"
//        }

        if (priceMin != null) {
            query += " AND price >= '$priceMin'"
        }

        if (priceMax != null) {
            query += " AND price <= '$priceMax'"
        }

        if (beginSearchDate != null) {
            query += " AND 'entry date' >= '$beginSearchDate'"
        }

        if (endSearchDate != null) {
            query += " AND 'entry date' <= '$endSearchDate'"
        }



        mViewModel.getEstatesBySearch(query)
            .observe(viewLifecycleOwner, androidx.lifecycle.Observer {
                if (it!!.isNotEmpty()) {
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
}