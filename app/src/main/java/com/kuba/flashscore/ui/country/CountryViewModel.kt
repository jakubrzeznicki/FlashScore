package com.kuba.flashscore.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.mappers.CountryDtoMapper
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class CountryViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    private val connectivityManager: ConnectivityManager,
    private val countryDtoMapper: CountryDtoMapper
) : ViewModel() {

    private val _countries = MutableLiveData<Event<Resource<List<CountryEntity>>>>()
    val countries: LiveData<Event<Resource<List<CountryEntity>>>> = _countries

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun getCountries() {
        _countries.value = Event(Resource.loading(null)).also {
            delay(1000)
        }
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getCountriesFromNetwork()
                    delay(100)
                    repository.insertCountries(countryDtoMapper.toLocalList(response.data?.toList()!!, null))
                    Timber.d("JUREK fetch cuntr form network")
                    _countries.value =
                        Event(Resource.success(countryDtoMapper.toLocalList(response.data.toList(), null)))
                } else {
                    val response = repository.getCountriesFromDb()
                    if (response.isNullOrEmpty()) {
                        Timber.d("JUREK fetch country form db coun erro")
                        _countries.value = Event(Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        Timber.d("JUREK fetch country form db coun succ ${response[0].countryName}")
                        _countries.value = Event(Resource.success(response))
                    }
                }
            }
        }
    }
}