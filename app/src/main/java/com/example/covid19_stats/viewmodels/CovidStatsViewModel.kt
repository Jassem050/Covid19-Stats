package com.example.covid19_stats.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.covid19_stats.database.CovidStatsDatabase
import com.example.covid19_stats.network.TimeLine
import com.example.covid19_stats.repositories.CovidStatsRepository
import kotlinx.coroutines.launch

class CovidStatsViewModel(application: Application): AndroidViewModel(application) {
//
//    private val viewModelJob = Job()
//    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = CovidStatsDatabase.getInstance(application)
    private val covidStatsRepository = CovidStatsRepository(database)

    private val _timeLine = MutableLiveData<TimeLine>()
    val timeLine: LiveData<TimeLine>
        get() = _timeLine

    private val _searchQuery = MutableLiveData<String>()
    val searchQuery: LiveData<String>
        get() = _searchQuery


    init {
        viewModelScope.launch {
            val timeLineResult = covidStatsRepository.getTimeline()
            _timeLine.value = timeLineResult[0]
            covidStatsRepository.getAllCountries()
        }

    }

    val countries = covidStatsRepository.countries

    fun setSearchQuery(query: String?) {
        _searchQuery.value = query
    }

    override fun onCleared() {
        super.onCleared()
//        viewModelJob.cancel()
    }
}