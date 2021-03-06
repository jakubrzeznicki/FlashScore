package com.kuba.flashscore.repositories.country

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.local.*
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.mappers.CountryDtoMapper
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

    override suspend fun refreshCountries(): Resource<List<CountryEntity>> {
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
                    return Resource.success(countryDao.getAllCountriesFromDb().value)
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

    override fun getCountriesFromDb(): LiveData<List<CountryEntity>> =
        countryDao.getAllCountriesFromDb()

}