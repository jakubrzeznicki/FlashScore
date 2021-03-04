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
) : ViewModel() {

    private val _teams = MutableLiveData<Event<Resource<CountryWithLeagueAndTeams>>>()
    val teams: LiveData<Event<Resource<CountryWithLeagueAndTeams>>> = _teams

    private val _team = MutableLiveData<Event<Resource<TeamWithPlayersAndCoach>>>()
    val team: LiveData<Event<Resource<TeamWithPlayersAndCoach>>> = _team

    fun getCountryWithLeagueAndTeams(leagueId: String) {
        viewModelScope.launch {
            val response =
                repository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId)
            _teams.value = Event(Resource.success(response))
        }
    }

    suspend fun getTeamWithPlayersAndCoach(teamId: String) {
        _team.value = Event(Resource.success(repository.getTeamWithPlayersAndCoachFromDb(teamId)))
    }
}