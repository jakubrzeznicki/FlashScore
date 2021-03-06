package com.kuba.flashscore.ui.player

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.data.network.responses.PlayerResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.player.PlayerRepository
import kotlinx.coroutines.launch

class PlayersViewModel @ViewModelInject constructor(
    private val repository: PlayerRepository
) : ViewModel() {

    private val _players = MutableLiveData<Event<Resource<PlayerResponse>>>()
    val players: LiveData<Event<Resource<PlayerResponse>>> = _players

    fun getPlayerBySpecificName(name: String) {
        _players.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getPlayerBySpecificName(name)
            _players.value = Event(response)
        }
    }
}