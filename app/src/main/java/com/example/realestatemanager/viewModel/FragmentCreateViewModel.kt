package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor


class FragmentCreateViewModel(app: Application,private val itemDataSource: EstateRepository,private val executor: Executor ): AndroidViewModel(app) {

    lateinit var  currentEstate: LiveData<EstateEntity>

    fun createEstate(estate: EstateEntity) {
        executor.execute { itemDataSource.insertEstate(estate) }
    }

    fun init(userId: Int) {
        currentEstate = itemDataSource.getEstate(userId)
    }

    fun getEstate(estateId: Int?): LiveData<EstateEntity> {
        return this.currentEstate
    }

    fun updateEstate (estate: EstateEntity){
        executor.execute { itemDataSource.updateEstate(estate)}
    }

}