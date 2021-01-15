package com.example.realestatemanager.injections

import android.content.Context
import com.example.realestatemanager.database.RoomDb
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor
import java.util.concurrent.Executors

object Injection {

    fun provideItemDataSource(context: Context): EstateRepository {
        val database = RoomDb.getAppDatabase(context)
        return EstateRepository(database)
    }

    fun provideExecutor(): Executor {
        return Executors.newSingleThreadExecutor()
    }

    fun provideViewModelFactory(context: Context): ViewModelFactory? {
        val dataSourceItem: EstateRepository = provideItemDataSource(context)
        val executor: Executor = provideExecutor()
        return ViewModelFactory(dataSourceItem, executor)
    }
}
