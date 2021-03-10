package com.kuba.flashscore.ui.teams

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.team.TeamRepository
import kotlinx.coroutines.launch

class TeamsViewModel @ViewModelInject constructor(
    private val repository: TeamRepository,
) : ViewModel() {

    private val _teams = MutableLiveData<CountryWithLeagueAndTeams>()
    val teams: LiveData<CountryWithLeagueAndTeams> = _teams

    private val _team = MutableLiveData<TeamWithPlayersAndCoach>()
    val team: LiveData<TeamWithPlayersAndCoach> = _team

    fun getCountryWithLeagueAndTeams(leagueId: String) {
        viewModelScope.launch {
            _teams.postValue(
                repository.getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId)
                    .asDomainModel()
            )
        }
    }

    suspend fun getTeamWithPlayersAndCoach(teamId: String) {
        _team.value =
            repository.getTeamWithPlayersAndCoachFromDb(teamId).asDomainModel()


    }
}