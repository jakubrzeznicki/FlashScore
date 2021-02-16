package com.kuba.flashscore.ui.country

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.network.responses.CountryResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import kotlinx.coroutines.launch

class CountryViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository
) : ViewModel() {

    private val _countries = MutableLiveData<Event<Resource<CountryResponse>>>()
    val countries: LiveData<Event<Resource<CountryResponse>>> = _countries

    fun getCountries() {
        _countries.value = Event(Resource.loading(null))
        viewModelScope.launch {
            val response = repository.getCountries()
            _countries.value = Event(response)
        }
    }
}