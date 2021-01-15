package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor

class FragmentDetailViewModel(
    app: Application,
    private val itemDataSource: EstateRepository,
    private val executor: Executor
): AndroidViewModel(app){

    lateinit var  currentEstate: LiveData<EstateEntity>

    fun init(userId: Int) {
        currentEstate = itemDataSource.getEstate(userId)
    }

    fun getEstate(userId: Int): LiveData<EstateEntity> {
        return this.currentEstate
    }

}