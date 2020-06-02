package com.example.covid19_stats.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.covid19_stats.database.CountryDB
import com.example.covid19_stats.database.CovidStatsDatabase
import com.example.covid19_stats.database.asDomainModel
import com.example.covid19_stats.domain.Country
import com.example.covid19_stats.network.Network
import com.example.covid19_stats.network.Status
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class SearchRepository(private val database: CovidStatsDatabase) {

    val countries: LiveData<List<Country>> =
        Transformations.map(database.countryDao.getCountries()) {
            it.asDomainModel()
        }

    suspend fun getCountryDetails(query: String): CountryDB {
        return withContext(Dispatchers.IO) {
            Log.d("searchRepo", query)
            val data = database.countryDao.getCountryStats(query)
            data
        }
    }

    suspend fun getCountryStats(alpha2: String): Status {
        val status = Network.covidService.getCountryStatus(alpha2)
        Log.d("searchRepo", status.cases)
        return status
    }
}