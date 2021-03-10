package com.kuba.flashscore.repositories.country

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.other.Resource

interface CountryRepository {
    suspend fun insertCountries(countries: List<CountryEntity>)
    suspend fun refreshCountries(): Resource<List<Country>>
    suspend fun getCountriesFromDb(): List<CountryEntity>
}
