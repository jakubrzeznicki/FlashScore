package com.kuba.flashscore.ui.teams.standings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.StandingEntity
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.network.mappers.StandingDtoMapper
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.other.ViewModelUtils
import com.kuba.flashscore.repositories.standing.StandingRepository
import com.kuba.flashscore.repositories.team.TeamRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch

class StandingsViewModel @ViewModelInject constructor(
    private val standingRepository: StandingRepository,
    private val connectivityManager: ConnectivityManager,
    private val standingDtoMapper: StandingDtoMapper
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
                    val response = standingRepository.getStandingsFromSpecificLeague(leagueId)
                    standingRepository.insertStandings(
                        standingDtoMapper.toLocalList(
                            response.data?.toList()!!,
                            null
                        )
                    )
                    _standings.value = Event(
                        Resource.success(
                            standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId)
                        )
                    )
                } else {
                    val response = standingRepository.getStandingsFromSpecificLeagueFromDb(leagueId)
                    if (response.isNullOrEmpty()) {
                        _standings.value =
                            Event(Resource.error(Constants.ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        _standings.value = Event(Resource.success(response))
                    }
                }

            }

        }
    }
}