package com.example.realestatemanager.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.realestatemanager.R
import com.example.realestatemanager.database.EstateEntity
import com.example.realestatemanager.injections.Injection
import com.example.realestatemanager.view.adapter.RecyclerAdapter
import com.example.realestatemanager.viewModel.FragmentListViewModel

class FragmentListEstate : Fragment() {

    private lateinit var listEstate : ArrayList<EstateEntity>
    private lateinit var adapter : RecyclerAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var mViewModel: FragmentListViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
       val view : View = inflater.inflate(R.layout.fragment_list_estate, container, false)
        recyclerView = view.findViewById(R.id.fragment_estate_list_recyclerview)
        mViewModel = ViewModelProviders.of(this, Injection.provideViewModelFactory(this.context!!)).get(
            FragmentListViewModel::class.java)
        configureRecyclerView()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel.getAllEstate().observe(viewLifecycleOwner, Observer<List<EstateEntity>> { updateUI(it!!) })
    }

    private fun configureRecyclerView(){
        listEstate =  ArrayList()
        adapter = RecyclerAdapter(listEstate)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = adapter
    }

    private fun updateUI(results:List<EstateEntity>){

        this.listEstate.clear()
        this.listEstate.addAll(results)
        adapter.setResults(results)
        adapter.notifyDataSetChanged()
    }
}