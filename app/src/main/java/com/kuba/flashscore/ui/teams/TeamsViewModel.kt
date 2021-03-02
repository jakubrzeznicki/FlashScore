package com.kuba.flashscore.ui.teams

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.local.models.entities.TeamWithPlayersAndCoach
import com.kuba.flashscore.network.mappers.CoachDtoMapper
import com.kuba.flashscore.network.mappers.PlayerDtoMapper
import com.kuba.flashscore.network.mappers.TeamDtoMapper
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.team.TeamRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.launch
import timber.log.Timber

class TeamsViewModel @ViewModelInject constructor(
    private val repository: TeamRepository,
    private val connectivityManager: ConnectivityManager,
    private val teamDtoMapper: TeamDtoMapper,
    private val coachDtoMapper: CoachDtoMapper,
    private val playerDtoMapper: PlayerDtoMapper
) : ViewModel() {

    private val _teams = MutableLiveData<Event<Resource<CountryWithLeagueAndTeams>>>()
    val teams: LiveData<Event<Resource<CountryWithLeagueAndTeams>>> = _teams

    private val _team = MutableLiveData<Event<Resource<TeamWithPlayersAndCoach>>>()
    val team: LiveData<Event<Resource<TeamWithPlayersAndCoach>>> = _team

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    fun getTeamsFormSpecificLeague(leagueId: String) {
        _teams.value = Event(Resource.loading(null))
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getTeamsFromSpecificLeagueFromNetwork(leagueId)
                    _teams.value = Event(response)
//                    repository.apply {
//                        insertTeams(
//                            teamDtoMapper.toLocalList(
//                                response.data?.toList()!!,
//                                leagueId
//                            )
//                        )
//                        response.data.toList().forEach { teamDtoItem ->
//                            insertPlayers(
//                                playerDtoMapper.toLocalList(
//                                    teamDtoItem.players,
//                                    teamDtoItem.teamKey
//                                )
//                            )
//                            insertCoaches(
//                                coachDtoMapper.toLocalList(
//                                    teamDtoItem.coaches,
//                                    teamDtoItem.teamKey
//                                )
//                            )
//                        }
//                    }
//                    _teams.value = Event(
//                        Resource.success(
//                            repository.getTeamsFromLeagueFromDb(leagueId)
//                        )
//                    )
                } else {
                    val response = repository.getTeamsFromLeagueFromDb(leagueId)
                    if (response.teams.isNullOrEmpty()) {
                        Timber.d("JUREK fetch teams form db coun erro")
                        _teams.value =
                            Event(Resource.error(Constants.ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        _teams.value = Event(Resource.success(response))
                    }
                }

            }
        }
    }

    suspend fun getTeamById(teamId: String) {
        _team.value = Event(Resource.success(repository.getTeamWithPlayersAndCoachFromDb(teamId)))
    }
}