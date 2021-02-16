package com.kuba.flashscore.ui.teams

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.TeamResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class TeamsViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _teams = MutableLiveData<Event<Resource<TeamResponse>>>()
    val teams: LiveData<Event<Resource<TeamResponse>>> = _teams

    fun getTeamsFormSpecificLeague(leagueId: String) {
        _teams.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getTeamsFromSpecificLeague(leagueId)
            _teams.value = Event(response)
        }
    }
}