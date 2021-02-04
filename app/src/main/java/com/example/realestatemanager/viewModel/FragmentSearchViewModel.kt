package com.example.realestatemanager.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.repository.EstateRepository
import java.util.concurrent.Executor

class FragmentSearchViewModel(app: Application, private val itemDataSource: EstateRepository, private val executor: Executor): AndroidViewModel(app)  {

    fun getEstatesBySearch(queryToConvert:String): LiveData<List<EstateEntity>> {
        val query = SimpleSQLiteQuery(queryToConvert)
        Log.e("GET_ESTATES_BY_SEARCH","Query to execute : ${query.sql}")
       // args.forEach {
       //     if (it is Long) Log.e("GET_ESTATES_BY_SEARCH", "Args : ${SimpleDateFormat("dd/MM/yyyy").format(Date(it))}")
        //    else Log.e("GET_ESTATES_BY_SEARCH", "Args : $it")
     //   }
        return itemDataSource.getSearchEstate(query)
    }
}