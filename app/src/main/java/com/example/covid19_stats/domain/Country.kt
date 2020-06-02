package com.example.covid19_stats.domain

data class Country(
    val name: String,
    val alpha2: String,
    val alpha3: String,
    val numeric: String,
    val latitude: String,
    val longitude: String
)