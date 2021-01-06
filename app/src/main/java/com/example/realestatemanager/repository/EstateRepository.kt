package com.example.realestatemanager.repository

import androidx.lifecycle.LiveData
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.database.RoomDb

class EstateRepository (private val database : RoomDb) {


    fun getAllEstateInfo(): LiveData<List<EstateEntity>> {
        return database.estateDao().getAllEstateInfo()
    }

     fun insertEstate(estate: EstateEntity) {
        database.estateDao().insertEstate(estate)
    }

    fun updateEstate(estate: EstateEntity?){
        database.estateDao().updateEstate(estate)
    }
}