package com.kuba.flashscore.ui.teams.standings

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.StandingResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class StandingsViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _standings = MutableLiveData<Event<Resource<StandingResponse>>>()
    val standings: LiveData<Event<Resource<StandingResponse>>> = _standings


    fun getStandingsFromSpecificLeague(leagueId: String) {
        _standings.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getStandingsFromSpecificLeague(leagueId)
            _standings.value = Event(response)
        }
    }

}