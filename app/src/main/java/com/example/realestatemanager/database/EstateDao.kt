package com.example.realestatemanager.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface EstateDao {

    @Query("SELECT * FROM estateInfo ORDER BY id DESC")
    fun getAllEstateInfo(): LiveData<List<EstateEntity>>

    @Query("SELECT * FROM estateInfo WHERE id = :userId")
    fun getEstate(userId: Int): LiveData<EstateEntity>

    @Insert
    fun insertEstate(estate: EstateEntity?)

    @Delete
    fun deleteEstate(estate: EstateEntity?)

    @Update
    fun updateEstate(estate: EstateEntity?)

 //   @RawQuery ("SELECT * FROM estateInfo WHERE price >= ")
   // }
}