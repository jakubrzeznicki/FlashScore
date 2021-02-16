package com.kuba.flashscore.ui.club

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

class ClubViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {
    private val _club = MutableLiveData<Event<Resource<TeamResponse>>>()
    val club: LiveData<Event<Resource<TeamResponse>>> = _club

    fun getClubById(clubId: String) {
        _club.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getTeamByTeamId(clubId)
            _club.value = Event(response)
        }
    }

}