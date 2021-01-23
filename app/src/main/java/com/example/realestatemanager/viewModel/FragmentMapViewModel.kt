package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.model.Result
import com.example.realestatemanager.repository.EstateRepository
import com.example.realestatemanager.repository.GeocodeRepository
import java.util.concurrent.Executor

class FragmentMapViewModel(app: Application, private val itemDataSource: EstateRepository,private val executor: Executor): AndroidViewModel(app) {

     val response : LiveData<Result>

    fun getAllEstate(): LiveData<List<EstateEntity>> {
        return itemDataSource.getAllEstateInfo()
    }

    private val repository = GeocodeRepository(app)

    init {
        response = repository.response
    }

    fun getGeocodedLocation( address : String, key : String) {
        repository.getGeocodedLocation(address, key)
    }
}