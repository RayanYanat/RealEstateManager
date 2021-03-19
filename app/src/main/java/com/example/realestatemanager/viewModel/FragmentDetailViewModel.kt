package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.model.Result
import com.example.realestatemanager.repository.EstateRepository
import com.example.realestatemanager.repository.GeocodeRepository
import java.util.concurrent.Executor

class FragmentDetailViewModel(
    app: Application,
    private val itemDataSource: EstateRepository,executor: Executor
): AndroidViewModel(app){

    lateinit var  currentEstate: LiveData<EstateEntity>
    private val repository = GeocodeRepository(app)
    val response : LiveData<Result>

    init {
        response = repository.response
    }



    fun init(estateId: Int) {
        currentEstate = itemDataSource.getEstate(estateId)
    }

    fun getEstate(userId: Int): LiveData<EstateEntity> {
        return this.currentEstate
    }

    fun getGeocodedLocation( address : String, key : String) {
        repository.getGeocodedLocation(address, key)
    }
}