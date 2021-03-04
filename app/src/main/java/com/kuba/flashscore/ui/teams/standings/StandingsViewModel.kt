package com.kuba.flashscore.ui.teams.standings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.StandingEntity
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.standing.StandingRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch

class StandingsViewModel @ViewModelInject constructor(
    private val standingRepository: StandingRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _standings = MutableLiveData<Event<Resource<List<StandingEntity>>>>()
    val standings: LiveData<Event<Resource<List<StandingEntity>>>> = _standings

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    fun getStandingsFromSpecificLeague(leagueId: String) {
        _standings.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = standingRepository.getStandingsFromSpecificLeagueFromNetwork(leagueId)
                    _standings.value = Event(response)
                } else {
                    val response = standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId)
                    if (response.isNullOrEmpty()) {
                        _standings.value =
                            Event(Resource.error(Constants.ERROR_MESSAGE, null))
                    } else {
                        _standings.value = Event(Resource.success(response))
                    }
                }

            }

        }
    }
}