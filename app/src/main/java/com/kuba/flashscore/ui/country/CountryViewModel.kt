package com.kuba.flashscore.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.country.CountryRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch

class CountryViewModel @ViewModelInject constructor(
    private val repository: CountryRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    val countriesList = repository.getCountriesFromDb()

    private val _countriesStatus = MutableLiveData<Event<Resource<List<CountryEntity>>>>()
    val countriesStatus: LiveData<Event<Resource<List<CountryEntity>>>> = _countriesStatus

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
                    _countriesStatus.value = Event(Resource.error("Not internet connection", null))
                    //val response = repository.getCountriesFromDb()
                    //_countriesStatus.value = Event(Resource.success(response))
                }
            }
        }
    }
}
