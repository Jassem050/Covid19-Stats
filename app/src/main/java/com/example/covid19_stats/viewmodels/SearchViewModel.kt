package com.example.covid19_stats.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.covid19_stats.database.CountryDB
import com.example.covid19_stats.database.CovidStatsDatabase
import com.example.covid19_stats.network.Status
import com.example.covid19_stats.repositories.SearchRepository
import kotlinx.coroutines.launch

class SearchViewModel(application: Application): AndroidViewModel(application) {

    private val database = CovidStatsDatabase.getInstance(application)
    private val repository = SearchRepository(database)

    val searchQuery = MutableLiveData<String>()
    private val _country = MutableLiveData<CountryDB>()
    val country: LiveData<CountryDB>
        get() = _country
    private val _alpha2 = MutableLiveData<String>()
    val alpha2: LiveData<String>
        get() = _alpha2

    private val _status = MutableLiveData<Status>()
    val status: LiveData<Status>
        get() = _status

    init {
        viewModelScope.launch {

        }
        Log.d("searchViewModel", "called")
    }

    val countries = repository.countries

    fun getCountryAlpha2() {
        viewModelScope.launch {
            searchQuery.value?.let {
                Log.d("searchViewModel", "$it in fun")
                _country.value = repository.getCountryDetails(it)
                Log.d("searchViewModel", "obj: ${country.value}")
                country.value?.let {
                    Log.d("searchViewModel", it.alpha2)
                }
                _alpha2.value = country.value?.alpha2
            }
        }
    }

    fun getCountryStats(alpha2: String) {
        viewModelScope.launch {
            _status.value = repository.getCountryStats(alpha2)
        }
    }

}