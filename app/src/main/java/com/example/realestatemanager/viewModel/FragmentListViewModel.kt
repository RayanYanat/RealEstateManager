package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.database.RoomDb
import com.example.realestatemanager.repository.EstateRepository

class FragmentListViewModel (app:Application): AndroidViewModel(app) {

    lateinit var allEstate : MutableLiveData<List<EstateEntity>>

    init {
        allEstate = MutableLiveData()
    }

    fun getAllEstateObservers(): MutableLiveData<List<EstateEntity>> {
        return allEstate
    }


    fun getAllEstate() {
    }
}