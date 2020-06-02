package com.example.covid19_stats.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


interface CovidStatsService {

    @GET("timeline")
    suspend fun getTimeLine(): List<TimeLine>

    @GET("countries")
    suspend fun getCountries(): List<Country>

    @GET("status/{country}")
    suspend fun getCountryStatus(@Path("country") country: String): Status
}

private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

object Network {
    private val retrofit = Retrofit.Builder()
        .baseUrl("https://covid19-api.org/api/")
        .addConverterFactory(MoshiConverterFactory.create(moshi))
        .build()

    val covidService = retrofit.create(CovidStatsService::class.java)
}