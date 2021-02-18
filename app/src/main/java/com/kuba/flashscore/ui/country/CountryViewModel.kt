package com.kuba.flashscore.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.mappers.CountryDtoMapper
import com.kuba.flashscore.other.Constants.ERROR_INTERNET_CONNECTION_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class CountryViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository,
    private val connectivityManager: ConnectivityManager,
    private val countryDtoMapper: CountryDtoMapper
) : ViewModel() {

    private val _countries = MutableLiveData<Event<Resource<List<CountryEntity>>>>()
    val countries: LiveData<Event<Resource<List<CountryEntity>>>> = _countries

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun getCountries() {
        _countries.value = Event(Resource.loading(null)).also {
            delay(100)
        }
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getCountriesFromNetwork()
                    repository.insertCountries(countryDtoMapper.toDomainList(response.data?.toList()!!))
                    _countries.value =
                        Event(Resource.success(countryDtoMapper.toDomainList(response.data.toList())))
                } else {
                    val response = repository.observeAllCountries()
                    if (response.value == null) {
                        _countries.value = Event(Resource.error(ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        _countries.value = Event(Resource.success(response.value))
                    }
                }
            }
        }
    }
}