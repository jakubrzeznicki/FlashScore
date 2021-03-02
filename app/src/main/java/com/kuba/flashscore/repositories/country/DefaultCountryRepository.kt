package com.kuba.flashscore.repositories.country

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.local.models.event.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultCountryRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val apiFootballService: ApiFootballService
) : CountryRepository {

    override suspend fun getCountriesFromNetwork(): Resource<CountryResponse> {
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let {
                    return@let Resource.success(
                        it
                    )
                } ?: Resource.error(ERROR_MESSAGE, null)
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null)
        }
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryDao.insertCountries(countries)
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryDao.getAllCountriesFromDb()
    }
}