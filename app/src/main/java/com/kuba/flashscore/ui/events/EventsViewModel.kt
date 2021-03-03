package com.kuba.flashscore.ui.events

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.network.mappers.CoachDtoMapper
import com.kuba.flashscore.network.mappers.PlayerDtoMapper
import com.kuba.flashscore.network.mappers.TeamDtoMapper
import com.kuba.flashscore.network.mappers.event.EventDtoMapper
import com.kuba.flashscore.network.responses.EventResponse
import com.kuba.flashscore.network.responses.TeamResponse
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.event.EventRepository
import com.kuba.flashscore.repositories.team.TeamRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel @ViewModelInject constructor(
    private val eventRepository: EventRepository,
    private val teamRepository: TeamRepository,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private val _countryWithLeagueWithTeamsAndEvents =
        MutableLiveData<Event<Resource<CountryWithLeagueWithEventsAndTeams>>>()
    val countryWithLeagueWithTeamsAndEvents: LiveData<Event<Resource<CountryWithLeagueWithEventsAndTeams>>> =
        _countryWithLeagueWithTeamsAndEvents

    private val _eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions =
        MutableLiveData<EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions>()
    val eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions:
            LiveData<EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions> =
        _eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions


    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange


    fun getCountryWithLeagueWithTeamsAndEvents(leagueId: String, from: String, to: String) {
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val eventResponse =
                        eventRepository.getEventsFromSpecificLeaguesFromNetwork(leagueId, from, to)
                    val teamsResponse =
                        teamRepository.getTeamsFromSpecificLeagueFromNetwork(leagueId)

                    if (eventResponse.status == Status.SUCCESS && teamsResponse.status == Status.SUCCESS) {

                        _countryWithLeagueWithTeamsAndEvents.postValue(Event(Resource.loading(null)))
                        _countryWithLeagueWithTeamsAndEvents.postValue(
                            Event(
                                Resource.success(
                                    eventRepository.getCountryWithLeagueWithTeamsAndEvents(
                                        leagueId,
                                        from,
                                        to
                                    )
                                )
                            )
                        )

                        Timber.d(
                            "JUREKKK ${
                                eventRepository.getCountryWithLeagueWithTeamsAndEvents(
                                    leagueId,
                                    from,
                                    to
                                ).leagueWithEvents[0].events.size
                            }"
                        )


                        Timber.d(
                            "JUREKKK ${
                                eventRepository.getCountryWithLeagueWithTeamsAndEvents(
                                    leagueId,
                                    from,
                                    to
                                ).leagueWithEvents[0].events.forEach { Timber.d("JUREKKKK event date: ${it?.matchDate}, event id: ${it?.matchId}") }
                            }"
                        )
                    } //else {
//                        _countryWithLeagueWithTeamsAndEvents.postValue(
//                            Event(
//                                Resource.error(
//                                    eventResponse.message ?: "Lack of data", null
//                                )
//                            )
//                        )
//                    }

                } else {
                    _countryWithLeagueWithTeamsAndEvents.postValue(Event(Resource.loading(null)))
                    val countryWithLeagueWithEventsAndTeams =
                        eventRepository.getCountryWithLeagueWithTeamsAndEvents(
                            leagueId,
                            from,
                            to
                        )
                    _countryWithLeagueWithTeamsAndEvents.postValue(
                        Event(
                            Resource.success(
                                countryWithLeagueWithEventsAndTeams
                            )
                        )
                    )
                }
            }
        }
    }

    fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data =
                eventRepository.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
                    eventId
                )
            _eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions.postValue(data)

        }
    }
}