package com.kuba.flashscore.ui.league

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.LeagueResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class LeagueViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _leagues = MutableLiveData<Event<Resource<LeagueResponse>>>()
    val leagues: LiveData<Event<Resource<LeagueResponse>>> = _leagues

    fun getLeaguesFromSpecificCountry(countryId: String) {
        _leagues.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getLeaguesFromSpecificCountry(countryId)
            _leagues.value = Event(response)
        }
    }

}