package com.kuba.flashscore.ui.league

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.network.mappers.LeagueDtoMapper
import com.kuba.flashscore.network.responses.LeagueResponse
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.league.LeagueRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class LeagueViewModel @ViewModelInject constructor(
    private val repository: LeagueRepository,
    private val connectivityManager: ConnectivityManager,
    private val leagueDtoMapper: LeagueDtoMapper
) : ViewModel() {

    private val _leaguesFromDb = MutableLiveData<Event<Resource<CountryAndLeagues>>>()
    val leaguesFromDb: LiveData<Event<Resource<CountryAndLeagues>>> = _leaguesFromDb

    private val _leaguesFromNetworkStatus = MutableLiveData<Event<Resource<LeagueResponse>>>()
    val leaguesFromNetworkStatus: LiveData<Event<Resource<LeagueResponse>>> = _leaguesFromNetworkStatus

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun getLeaguesFromSpecificCountry(countryId: String) {
        _leaguesFromNetworkStatus.value = Event(Resource.loading(null)).also {
            delay(100)
        }
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getLeaguesFromSpecificCountryFromNetwork(countryId)
                    _leaguesFromNetworkStatus.postValue(Event(response))
                    delay(100)
                    repository.insertLeagues(leagueDtoMapper.toLocalList(response.data?.toList()!!, null))
                    _leaguesFromDb.value = Event(Resource.success(repository.getLeagueFromSpecificCountryFromDb(countryId)))
                } else {
                    val response = repository.getLeagueFromSpecificCountryFromDb(countryId)
                    if (response.leagues.isNullOrEmpty()) {
                        Timber.d("JUREK fetch league form db coun erro")
                        _leaguesFromDb.value =
                            Event(Resource.error(Constants.ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        Timber.d("JUREK fetch leagues form db league succ ${response.leagues[0].leagueName}")
                        _leaguesFromDb.value = Event(Resource.success(response))
                    }
                }
            }
        }
    }


}