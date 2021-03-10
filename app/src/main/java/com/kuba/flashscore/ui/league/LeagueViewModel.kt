package com.kuba.flashscore.ui.league

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.league.LeagueRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch
import timber.log.Timber

class LeagueViewModel @ViewModelInject constructor(
    private val repository: LeagueRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private var _countriesWithLeagues = MutableLiveData<CountryAndLeagues>()
    val countriesWithLeagues: LiveData<CountryAndLeagues> = _countriesWithLeagues

    private val _countryWithLeaguesStatus = MutableLiveData<Event<Resource<CountryAndLeagues>>>()
    val countryWithLeaguesStatus: LiveData<Event<Resource<CountryAndLeagues>>> =
        _countryWithLeaguesStatus

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun refreshCountryWithLeagues(countryId: String) {
        _countryWithLeaguesStatus.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.refreshLeaguesFromSpecificCountry(countryId)
                    _countryWithLeaguesStatus.value = Event(response)
                } else {
                    _countryWithLeaguesStatus.value = Event(Resource.error(ERROR_MESSAGE, null))
                }
            }
        }
    }

    fun getCountryWithLeagues(countryId: String) {
        viewModelScope.launch {
            _countriesWithLeagues.postValue(
                repository.getLeagueFromSpecificCountryFromDb(countryId).asDomainModel()
            )
        }
    }

}