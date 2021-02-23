package com.kuba.flashscore.ui.league

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.mappers.CountryDtoMapper
import com.kuba.flashscore.network.mappers.LeagueDtoMapper
import com.kuba.flashscore.network.responses.LeagueResponse
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.FlashScoreRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class LeagueViewModel @ViewModelInject constructor(
    private val repository: FlashScoreRepository,
    private val connectivityManager: ConnectivityManager,
    private val leagueDtoMapper: LeagueDtoMapper
) : ViewModel() {

    private val _leagues = MutableLiveData<Event<Resource<CountryAndLeagues>>>()
    val leagues: LiveData<Event<Resource<CountryAndLeagues>>> = _leagues

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange

    suspend fun getLeaguesFromSpecificCountry(countryId: String) {
        _leagues.value = Event(Resource.loading(null)).also {
            delay(100)
        }
        viewModelScope.launch {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response = repository.getLeaguesFromSpecificCountry(countryId)
                    delay(100)
                    repository.insertLeagues(leagueDtoMapper.toDomainList(response.data?.toList()!!))
                    Timber.d("JUREK fetch leagues form network")
                    _leagues.value =
                        Event(Resource.success(
                            CountryAndLeagues(
                                CountryEntity(
                                    response.data[0].countryId,
                                    response.data[0].countryLogo,
                                    response.data[0].countryName
                                ),
                                leagueDtoMapper.toDomainList(response.data.toList())
                            )
                        ))
                } else {
                    val response = repository.getLeagueFromSpecificCountryFromDb(countryId)
                    if (response.leagues.isNullOrEmpty()) {
                        Timber.d("JUREK fetch league form db coun erro")
                        _leagues.value =
                            Event(Resource.error(Constants.ERROR_INTERNET_CONNECTION_MESSAGE, null))
                    } else {
                        Timber.d("JUREK fetch leagues form db league succ ${response.leagues[0].leagueName}")
                        _leagues.value = Event(Resource.success(response))
                    }
                }
            }
        }
    }


}