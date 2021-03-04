package com.kuba.flashscore.repositories.country

import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.CountryDtoMapper
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import java.lang.Exception
import javax.inject.Inject

class DefaultCountryRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val countryDtoMapper: CountryDtoMapper,
    private val apiFootballService: ApiFootballService
) : CountryRepository {

    override suspend fun getCountriesFromNetwork(): Resource<List<CountryEntity>> {
        return try {
            val response = apiFootballService.getCountries()
            if (response.isSuccessful) {
                response.body().let {
                    insertCountries(
                        countryDtoMapper.toLocalList(
                            it?.toList()!!,
                            null
                        )
                    )
                    return Resource.success(countryDao.getAllCountriesFromDb())
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        countryDao.insertCountries(countries)
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> {
        return countryDao.getAllCountriesFromDb()
    }
}