package com.kuba.flashscore.repositories.country

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.models.CountryDto
import com.kuba.flashscore.data.network.responses.CountryResponse
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Status
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.InOrder
import org.mockito.Mockito.mock
import retrofit2.Response
import timber.log.Timber
import javax.annotation.Resource

@ExperimentalCoroutinesApi
class DefaultCountryRepositoryTest {

    //Test subject
    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var countryRepository: CountryRepository

    //Collaborators
    private lateinit var countryDao: CountryDao
    private lateinit var footballApiService: ApiFootballService

    //Utilities
    lateinit var countryFromApi: Response<CountryResponse>
    lateinit var countryFromDao: CountryEntity

    // private val country1 = CountryEntity("countryId1", "countryLogo1", "countryName1")
    @Before
    fun setup() = runBlockingTest {
        //mocking dao
        countryDao = mock()
        countryFromDao = CountryEntity("countryId1", "countryLogo1", "countryName1")
        whenever(countryDao.getAllCountriesFromDb()).thenReturn(listOf(countryFromDao))

        //mocking api
        footballApiService = mock()
        countryFromApi = produceCountryResponseSuccess()
        whenever(footballApiService.getCountries()).thenReturn(countryFromApi)

        countryRepository = DefaultCountryRepository(countryDao, footballApiService)
    }

    @Test
    fun getUsersFromDatabase_shouldReturnUsers() = runBlockingTest {
        countryRepository.getCountriesFromDb()
        verify(countryDao, times(1)).getAllCountriesFromDb()
    }

    @Test
    fun getUsersFromDatabaseAndUsersExists_shouldNotCallToApiService() = runBlockingTest {
        countryRepository.getCountriesFromDb()
        verify(footballApiService, never()).getCountries()
    }


    @Test
    fun ifDaoReturnsCountriesThenRepositoryReturnsSameCountries() = runBlockingTest {
        val countries = countryRepository.getCountriesFromDb()
        assertThat(countries).hasSize(1)
        assertThat(countries).contains(countryFromDao)
        assertThat(countries[0].countryId).isEqualTo("countryId1")
        assertThat(countries[0].countryLogo).isEqualTo("countryLogo1")
        assertThat(countries[0].countryName).isEqualTo("countryName1")
    }

    @Test
    fun getCountriesFromDatabaseIsEmpty_shouldCallToApiService() = runBlockingTest {
        whenever(countryDao.getAllCountriesFromDb()).thenReturn(null)
        countryRepository.refreshCountries()

        verify(countryDao, times(1)).getAllCountriesFromDb()
        verify(footballApiService, times(1)).getCountries()
    }


    @Test
    fun apiServiceIsCallBeforeCallCountryDaoIfDatabaseIsEmpty() = runBlockingTest {
        whenever(countryDao.getAllCountriesFromDb()).thenReturn(null)
        countryRepository.refreshCountries()

        val orderVerifier: InOrder = inOrder(footballApiService, countryDao)
        orderVerifier.verify(footballApiService).getCountries()
        orderVerifier.verify(countryDao).getAllCountriesFromDb()
    }

    @Test
    fun refreshCountriesFromNetwork_countriesShouldAlsoInsertToDatabase() = runBlockingTest {
        whenever(countryDao.getAllCountriesFromDb()).thenReturn(null)
        countryRepository.refreshCountries()

        verify(countryDao, times(1)).insertCountries(any())
        whenever(countryDao.getAllCountriesFromDb()).thenReturn(listOf(countryFromDao))

        val countries = countryRepository.getCountriesFromDb()
        assertThat(countries).hasSize(1)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeError_returnError() = runBlockingTest {
        whenever(footballApiService.getCountries()).thenReturn(produceCountryResponseError())
        val countries = countryRepository.refreshCountries()

        verify(countryDao, never()).getAllCountriesFromDb()

        verify(footballApiService, times(1)).getCountries()
        assertThat(countries.status).isEqualTo(Status.ERROR)
        assertThat(countries.data).isEqualTo(null)
        assertThat(countries.message).isEqualTo(ERROR_MESSAGE)
    }

    @Test
    fun whenWhileFetchDataFromNetworkOccurredSomeErrorAndDatabaseIsAlsoEmpty_returnError() =
        runBlockingTest {
            whenever(footballApiService.getCountries()).thenReturn(produceCountryResponseError())
            whenever(countryDao.getAllCountriesFromDb()).thenReturn(null)
            countryRepository.refreshCountries()
            val countriesFromDatabase = countryRepository.getCountriesFromDb()

            verify(countryDao, times(1)).getAllCountriesFromDb()
            verify(footballApiService, times(1)).getCountries()
            assertThat(countriesFromDatabase).isNull()
        }

    @Test
    fun insertCountriesIntoDb_shouldReturnSuccess() = runBlockingTest {
        val countriesList = listOf<CountryEntity>(
            CountryEntity("countryId1", "countryLogo1", "countryName1"),
            CountryEntity("countryId2", "countryLogo2", "countryName2"),
            CountryEntity("countryId3", "countryLogo3", "countryName3")
        )

        countryRepository.insertCountries(countriesList)
        verify(countryDao, times(1)).insertCountries(countriesList)
    }

    private fun produceCountryResponseSuccess(): Response<CountryResponse> {
        val countryResponse = CountryResponse()
        countryResponse.add(0, CountryDto("countryId2", "countryLogo2", "countryName2"))
        return Response.success(countryResponse)
    }

    private fun produceCountryResponseError(): Response<CountryResponse> {
        val errorResponse =
            "{\n" +
                    "  \"type\": \"error\",\n" +
                    "  \"message\": \"What you were looking for isn't here.\"\n" + "}"
        val errorResponseBody = errorResponse.toResponseBody("application/json".toMediaTypeOrNull())
        return Response.error(400, errorResponseBody)
    }
}