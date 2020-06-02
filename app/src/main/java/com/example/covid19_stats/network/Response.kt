package com.example.covid19_stats.network

import com.squareup.moshi.Json

data class TimeLine(
    @Json(name = "last_update") val lastUpdated: String,
    @Json(name = "total_cases") val totalCases: String,
    @Json(name = "total_deaths") val totalDeaths: String,
    @Json(name = "total_recovered") val totalRecovered: String
)

