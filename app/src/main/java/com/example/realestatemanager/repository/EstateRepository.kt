package com.example.realestatemanager.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.database.RoomDb

class EstateRepository(private val database: RoomDb) {


    fun getAllEstateInfo(): LiveData<List<EstateEntity>> {
        return database.estateDao().getAllEstateInfo()
    }

     fun insertEstate(estate: EstateEntity) {
        database.estateDao().insertEstate(estate)
    }

    fun getSearchEstate(query: SupportSQLiteQuery): LiveData<List<EstateEntity>>{
      return database.estateDao().getSearchEstate(query)
    }

    fun updateEstate(estate: EstateEntity?){
        database.estateDao().updateEstate(estate)
    }

    fun getEstate(userId: Int): LiveData<EstateEntity> {
        return database.estateDao().getEstate(userId)
    }
}