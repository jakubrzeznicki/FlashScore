package com.kuba.flashscore.ui.league

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.data.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.league.LeagueRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch

class LeagueViewModel @ViewModelInject constructor(
    private val repository: LeagueRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _leagues = MutableLiveData<Event<Resource<CountryAndLeagues>>>()
    val leagues: LiveData<Event<Resource<CountryAndLeagues>>> = _leagues

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun getLeaguesFromSpecificCountry(countryId: String) {
        _leagues.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getLeaguesFromSpecificCountryFromNetwork(countryId)
                    _leagues.value = Event(response)
                } else {
                    val response = repository.getLeagueFromSpecificCountryFromDb(countryId)
                    if (response.leagues.isNullOrEmpty()) {
                        _leagues.value =
                            Event(Resource.error(ERROR_MESSAGE, null))
                    } else {
                        _leagues.value = Event(Resource.success(response))
                    }
                }
            }
        }
    }


}