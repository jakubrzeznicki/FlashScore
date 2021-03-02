package com.kuba.flashscore.repositories.country

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface CountryRepository {
    suspend fun insertCountries(countries: List<CountryEntity>)
    suspend fun getCountriesFromNetwork(): Resource<CountryResponse>
    suspend fun getCountriesFromDb(): List<CountryEntity>
}
