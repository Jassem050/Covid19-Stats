package com.example.covid19_stats.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(countries: List<CountryDB>)

    @Query("SELECT * from countries")
    fun getCountries(): LiveData<List<CountryDB>>

    @Query("SELECT * from countries WHERE name = :query")
    fun getCountryStats(query: String): CountryDB
}

@Database(entities = [CountryDB::class], version = 1, exportSchema = false)
abstract class CovidStatsDatabase : RoomDatabase() {
    abstract val countryDao: CountryDao

    companion object {

        @Volatile
        private var INSTANCE: CovidStatsDatabase? = null
        fun getInstance(context: Context): CovidStatsDatabase {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CovidStatsDatabase::class.java,
                        "covid_stats_database"
                    )
                        .fallbackToDestructiveMigration()
                        .build()
                }
                return instance

            }
        }
    }
}