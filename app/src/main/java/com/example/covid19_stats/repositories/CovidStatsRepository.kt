package com.example.covid19_stats.repositories

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.covid19_stats.database.CovidStatsDatabase
import com.example.covid19_stats.database.asDomainModel
import com.example.covid19_stats.domain.Country
import com.example.covid19_stats.network.Network
import com.example.covid19_stats.network.TimeLine
import com.example.covid19_stats.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CovidStatsRepository(private val database: CovidStatsDatabase) {

    val countries: LiveData<List<Country>> = Transformations.map(database.countryDao.getCountries()) {
        it.asDomainModel()
    }

    suspend fun getTimeline(): List<TimeLine> {
        return Network.covidService.getTimeLine()
    }

    suspend fun getAllCountries() {
        withContext(Dispatchers.IO) {
            val countries = Network.covidService.getCountries()
            database.countryDao.insertAll(countries = countries.asDatabaseModel())
        }
    }

}