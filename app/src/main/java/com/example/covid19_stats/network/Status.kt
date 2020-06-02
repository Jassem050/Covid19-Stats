package com.example.covid19_stats.network

import com.squareup.moshi.Json

data class Status(
    val country: String,
    @Json(name = "last_update") val lastUpdated: String,
    val cases: String,
    val deaths: String,
    val recovered: String
    )