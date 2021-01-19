package com.example.realestatemanager.repository

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import com.example.realestatemanager.model.Result
import com.example.realestatemanager.utils.GeocodeService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.net.ssl.SSLEngineResult

class GeocodeRepository(val application: Application) {

    val response = MutableLiveData<Result>()

    fun getGeocodedLocation(address: String, key:String){

        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.google.com/maps/api/geocode/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(GeocodeService::class.java)

        service.getGeocodeLocation(address,key).enqueue(object  : Callback<Result> {
            override fun onFailure(call: Call<Result>, t: Throwable) {
                Toast.makeText(application,"Error wile accessing the API", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Result>, resp: Response<Result>) {
                response.value = resp.body()
            }

        })
    }
}