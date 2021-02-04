package com.example.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [EstateEntity::class], version = 1)

@TypeConverters(Converters::class)

abstract class RoomDb : RoomDatabase(){

    abstract fun estateDao(): EstateDao

    companion object {
        private var INSTANCE: RoomDb? = null

        fun getAppDatabase(context: Context): RoomDb {

            if (INSTANCE == null) {

                INSTANCE = Room.databaseBuilder<RoomDb>(
                    context.applicationContext, RoomDb::class.java, "EstateDB"
                )
                    .allowMainThreadQueries()
                    .build()

            }
            return INSTANCE as RoomDb
        }
    }


}