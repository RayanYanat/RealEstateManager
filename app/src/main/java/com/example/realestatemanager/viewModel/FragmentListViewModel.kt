package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor

class FragmentListViewModel (app:Application, private val itemDataSource: EstateRepository, private val executor: Executor): AndroidViewModel(app) {

     private var allEstate : MutableLiveData<List<EstateEntity>> = MutableLiveData()

    fun getAllEstateObservers(): MutableLiveData<List<EstateEntity>> {
        return allEstate
    }


    fun getAllEstate(): LiveData<List<EstateEntity>> {
        return itemDataSource.getAllEstateInfo()
    }
}