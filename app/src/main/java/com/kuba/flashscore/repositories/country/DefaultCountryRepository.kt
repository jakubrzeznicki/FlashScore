package com.kuba.flashscore.repositories.country

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.other.wrapEspressoIdlingResource
import timber.log.Timber
import java.lang.Exception
import javax.inject.Inject

class DefaultCountryRepository @Inject constructor(
    private val countryDao: CountryDao,
    private val apiFootballService: ApiFootballService
) : CountryRepository {

    override suspend fun refreshCountries(): Resource<List<Country>> {
        return try {
            val response = apiFootballService.getCountries()
            Timber.d("COUNTRY in repository isSuccessful? ${response.isSuccessful}")
            if (response.isSuccessful) {
                response.body().let { countryResponse ->
                    insertCountries(
                        countryResponse?.toList()?.map { it.asLocalModel() }!!
                    )
                    return Resource.success(
                        countryDao.getAllCountriesFromDb().map { it.asDomainModel() })
                }
            } else {
                Resource.error(ERROR_MESSAGE, null)
            }
        } catch (e: Exception) {
            Resource.error(ERROR_MESSAGE_LACK_OF_DATA, null)
        }
    }

    override suspend fun insertCountries(countries: List<CountryEntity>) {
        wrapEspressoIdlingResource {
            countryDao.insertCountries(countries)
        }
    }

    override suspend fun getCountriesFromDb(): List<CountryEntity> =
        wrapEspressoIdlingResource {
            countryDao.getAllCountriesFromDb()
        }
}