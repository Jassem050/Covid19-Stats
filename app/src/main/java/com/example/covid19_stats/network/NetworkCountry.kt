package com.example.covid19_stats.network

import com.example.covid19_stats.database.CountryDB

data class Country(
    val name: String,
    val alpha2: String,
    val alpha3: String,
    val numeric: String,
    val latitude: String,
    val longitude: String
)

fun List<Country>.asDatabaseModel(): List<CountryDB> {
    return map {
         CountryDB(
            name = it.name,
            alpha2 = it.alpha2,
            alpha3 = it.alpha3,
            numeric = it.numeric,
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}