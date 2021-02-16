package com.kuba.flashscore.ui.events

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.EventResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class EventsViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _events = MutableLiveData<Event<Resource<EventResponse>>>()
    val events: LiveData<Event<Resource<EventResponse>>> = _events

    fun getEventsFromSpecificLeague(leagueId: String, from: String, to: String) {
        _events.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getEventsFromSpecificLeagues(leagueId, from, to)
            _events.value = Event(response)
        }
    }
}