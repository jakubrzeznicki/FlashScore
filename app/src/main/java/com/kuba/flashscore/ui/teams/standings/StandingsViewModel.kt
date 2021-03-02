package com.kuba.flashscore.ui.teams.standings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.StandingEntity
import com.kuba.flashscore.network.mappers.StandingDtoMapper
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.standing.StandingRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch
import timber.log.Timber

class StandingsViewModel @ViewModelInject constructor(
    private val repository: StandingRepository,
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
                    val response = repository.getStandingsFromSpecificLeague(leagueId)
                    repository.insertStandings(
                        standingDtoMapper.toLocalList(
                            response.data?.toList()!!,
                            null
                        )
                    )
                    _standings.value = Event(
                        Resource.success(
                            standingDtoMapper.toLocalList(response.data.toList()!!, null)
                        )
                    )
                } else {
                    val response = repository.getStandingsFromSpecificLeagueFromDb(leagueId)
                    if (response.isNullOrEmpty()) {
                        Timber.d("JUREK fetch standing form db coun erro")
                        _standings.value =
                            Event(Resource.error(Constants.ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        Timber.d("JUREK fetch teams form db league succ ${response[0].awayLeagueD}")
                        _standings.value = Event(Resource.success(response))
                    }
                }

            }

        }
    }

}