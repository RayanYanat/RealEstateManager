package com.example.realestatemanager.utils;

import com.example.realestatemanager.model.Result;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GeocodeService {


    @GET("json?")
    Call<Result> getGeocodeLocation (@Query("address")String address,@Query("key") String key);



}