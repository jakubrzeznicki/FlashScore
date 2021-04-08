package com.kuba.flashscore.repositories

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.FakeApiFootballService
import com.kuba.flashscore.data.network.responses.*
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository
import java.lang.Exception

class FakeCountryRepositoryAndroidTest : CountryRepository {

    lateinit var apiFootballService: FakeApiFootballService
    private val countryItems = mutableListOf<CountryEntity>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    @Throws(UninitializedPropertyAccessException::class)
    override suspend fun refreshCountries(): Resource<List<Country>> {
//        return if (shouldReturnNetworkError) {
//            Resource.error("Error", null)
//        } else {
//            Resource.success(countryItems.map { it.asDomainModel() })
//        }
        throwExceptionIfApiServiceNotInitialzied()
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let { countryResponse ->
                    insertCountries(
                        countryResponse?.toList()?.map { it.asLocalModel() }!!
                    )
                    return Resource.success(countryItems.map { it.asDomainModel() })
                }
            } else {
                Resource.error(Constants.ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(Constants.ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryItems
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryItems.addAll(countries)
    }
//
//    override suspend fun  (): List<CountryEntity> {
//        return countryItems
//    }

    private fun throwExceptionIfApiServiceNotInitialzied(){
        if(!::apiFootballService.isInitialized){
            throw UninitializedPropertyAccessException(
                "Did you forget to set the ApiService in FakeMainRepositoryImpl?"
            )
        }
    }
}
