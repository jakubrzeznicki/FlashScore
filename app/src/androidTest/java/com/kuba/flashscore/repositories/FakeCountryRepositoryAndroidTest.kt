package com.kuba.flashscore.repositories

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository

class FakeCountryRepositoryAndroidTest : CountryRepository {

    private val countryItems = mutableListOf<CountryEntity>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun refreshCountries(): Resource<List<Country>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(countryItems.map { it.asDomainModel() })
        }
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryItems
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryItems.addAll(countries)
    }
}
