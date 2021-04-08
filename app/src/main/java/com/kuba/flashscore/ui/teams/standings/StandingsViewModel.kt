package com.kuba.flashscore.ui.teams.standings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.local.models.entities.StandingType
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.standing.StandingRepository
import com.kuba.flashscore.ui.util.networking.ConnectivityManager
import kotlinx.coroutines.launch

class StandingsViewModel @ViewModelInject constructor(
    private val standingRepository: StandingRepository,
    private val connectivityManager: ConnectivityManager
) : ViewModel() {

    private val _standingsStatus = MutableLiveData<Event<Resource<List<Standing>>>>()
    val standingsStatus: LiveData<Event<Resource<List<Standing>>>> = _standingsStatus

    private val _standings = MutableLiveData<List<Standing>>()
    val standings: LiveData<List<Standing>> = _standings

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun refreshStandingsFromSpecificLeague(leagueId: String) {
        _standingsStatus.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = standingRepository.refreshStandingsFromSpecificLeague(leagueId)
                    _standingsStatus.value = Event(response)
                } else {
                    Event(Resource.error(ERROR_MESSAGE, null))
                }
            }
        }
    }

    fun getOverallStandingsFromSpecificLeague(leagueId: String) {
        viewModelScope.launch {
            _standings.postValue(
                standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId, StandingType.OVERALL).map { it.asDomainModel() }
            )
        }
    }

    fun getHomeStandingsFromSpecificLeague(leagueId: String) {
        viewModelScope.launch {
            _standings.postValue(
                standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId, StandingType.HOME).map { it.asDomainModel() }
            )
        }
    }

    fun getAwayStandingsFromSpecificLeague(leagueId: String) {
        viewModelScope.launch {
            _standings.postValue(
                standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId, StandingType.AWAY).map { it.asDomainModel() }
            )
        }
    }
}