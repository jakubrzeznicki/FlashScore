package com.kuba.flashscore.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository
import com.kuba.flashscore.ui.util.networking.ConnectivityManager
import kotlinx.coroutines.launch

class CountryViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private var _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _countriesStatus = MutableLiveData<Event<Resource<List<Country>>>>()
    val countriesStatus: LiveData<Event<Resource<List<Country>>>> = _countriesStatus

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun refreshCountries() {
        _countriesStatus.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.refreshCountries()
                    _countriesStatus.value = Event(response)
                } else {
                    _countriesStatus.value = Event(Resource.error(ERROR_MESSAGE, null))
                }
            }
        }
    }

    fun getCountries() {
        viewModelScope.launch {
            _countries.postValue(
                repository.getCountriesFromDb().map { it.asDomainModel() }
            )
        }
    }

}
