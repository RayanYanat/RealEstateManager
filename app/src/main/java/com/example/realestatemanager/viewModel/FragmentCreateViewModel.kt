package com.example.realestatemanager.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor


class FragmentCreateViewModel(app: Application,private val itemDataSource: EstateRepository,private val executor: Executor ): AndroidViewModel(app) {


    fun createEstate(estate: EstateEntity) {
        executor.execute { itemDataSource.insertEstate(estate) }
    }

}