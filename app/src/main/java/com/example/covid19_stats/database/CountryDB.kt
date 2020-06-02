package com.example.covid19_stats.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.covid19_stats.domain.Country

@Entity(tableName = "countries")
class CountryDB(
    @PrimaryKey
    val name: String,
    val alpha2: String,
    val alpha3: String,
    val numeric: String,
    val latitude: String,
    val longitude: String
)

fun List<CountryDB>.asDomainModel(): List<Country> {
    return map {
        Country (
            name = it.name,
            alpha2 = it.alpha2,
            alpha3 = it.alpha3,
            numeric = it.numeric,
            latitude = it.latitude,
            longitude = it.longitude
        )
    }
}

