package com.example.covid19_stats.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class CovidStatsViewModelFactory(val app: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CovidStatsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CovidStatsViewModel(app) as T
        }
        throw IllegalArgumentException("Unable to construct viewmodel")
    }
}