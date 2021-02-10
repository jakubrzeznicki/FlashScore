package com.kuba.flashscore.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class FlashScoreViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _countries = MutableLiveData<Event<Resource<CountryResponse>>>()
    val countries: LiveData<Event<Resource<CountryResponse>>> = _countries

    private val _leagues = MutableLiveData<Event<Resource<LeagueResponse>>>()
    val leagues: LiveData<Event<Resource<LeagueResponse>>> = _leagues

    private val _teams = MutableLiveData<Event<Resource<TeamResponse>>>()
    val teams: LiveData<Event<Resource<TeamResponse>>> = _teams

    private val _team = MutableLiveData<Event<Resource<TeamResponse>>>()
    val team: LiveData<Event<Resource<TeamResponse>>> = _team

    private val _standings = MutableLiveData<Event<Resource<StandingResponse>>>()
    val standings: LiveData<Event<Resource<StandingResponse>>> = _standings

    private val _players = MutableLiveData<Event<Resource<PlayerResponse>>>()
    val players: LiveData<Event<Resource<PlayerResponse>>> = _players

    private val _events = MutableLiveData<Event<Resource<EventResponse>>>()
    val events: LiveData<Event<Resource<EventResponse>>> = _events

    fun getCountries() {
        _countries.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getCountries()
            _countries.value = Event(response)
        }
    }

    fun getLeaguesFromSpecificCountry(countryId: String) {
        _leagues.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getLeaguesFromSpecificCountry(countryId)
            _leagues.value = Event(response)
        }
    }

    fun getTeamsFormSpecificLeague(leagueId: String) {
        _teams.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getTeamsFromSpecificLeague(leagueId)
            _teams.value = Event(response)
        }
    }

    fun getTeamByTeamId(teamId: String) {
        _teams.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getTeamByTeamId(teamId)
            _team.value = Event(response)
        }
    }


    fun getStandingsFromSpecificLeague(leagueId: String) {
        _standings.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getStandingsFromSpecificLeague(leagueId)
            _standings.value = Event(response)
        }
    }

    fun getPlayerBySpecificName(name: String) {
        _players.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getPlayerBySpecificName(name)
            _players.value = Event(response)
        }
    }

    fun getEventsFromSpecificLeague(leagueId: String, from: String, to: String) {
        _events.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getEventsFromSpecificLeagues(leagueId, from, to)
            _events.value = Event(response)
        }
    }


}