package com.example.realestatemanager.injections

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.realestatemanager.repository.EstateRepository
import com.example.realestatemanager.viewModel.*
import java.util.concurrent.Executor


class ViewModelFactory(private val itemDataSource: EstateRepository, private val executor: Executor):ViewModelProvider.Factory {


    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            (modelClass.isAssignableFrom(FragmentCreateViewModel::class.java)) -> {
                FragmentCreateViewModel(Application(), itemDataSource, executor)
            }

            (modelClass.isAssignableFrom(FragmentListViewModel::class.java)) -> {
                FragmentListViewModel(Application(), itemDataSource, executor)
            }

            (modelClass.isAssignableFrom(FragmentDetailViewModel::class.java)) -> {
                FragmentDetailViewModel(Application(), itemDataSource, executor)
            }

            (modelClass.isAssignableFrom(FragmentMapViewModel::class.java)) -> {
                FragmentMapViewModel(Application(), itemDataSource, executor)
            }

            (modelClass.isAssignableFrom(FragmentSearchViewModel::class.java)) -> {
                FragmentSearchViewModel(Application(), itemDataSource, executor)
            }

            else -> throw IllegalArgumentException("Unknown ViewModel class")

        } as T
    }
}