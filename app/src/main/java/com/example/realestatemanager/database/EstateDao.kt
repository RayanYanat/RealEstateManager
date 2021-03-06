package com.example.realestatemanager.database

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery

@Dao
interface EstateDao {

    @Query("SELECT * FROM estateInfo ORDER BY id DESC")
    fun getAllEstateInfo(): LiveData<List<EstateEntity>>

    @Query("SELECT * FROM estateInfo WHERE id = :userId")
    fun getEstate(userId: Int): LiveData<EstateEntity>

    @Query("SELECT * FROM estateInfo WHERE id = :userId")
    fun getItemsWithCursor(userId: Long): Cursor

    @RawQuery(observedEntities = [EstateEntity::class])
    fun  getSearchEstate(query: SupportSQLiteQuery) : LiveData<List<EstateEntity>>

    @Insert
    fun insertEstate(estate: EstateEntity?) : Long

    @Delete
    fun deleteEstate(estate: EstateEntity?)

    @Update
    fun updateEstate(estate: EstateEntity?)

}