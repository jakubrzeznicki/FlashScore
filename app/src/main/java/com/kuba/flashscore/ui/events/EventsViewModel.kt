package com.kuba.flashscore.ui.events

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.local.models.entities.TeamWithPlayersAndCoach
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.other.*
import com.kuba.flashscore.repositories.event.EventRepository
import com.kuba.flashscore.repositories.player.PlayerRepository
import com.kuba.flashscore.repositories.team.TeamRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel @ViewModelInject constructor(
    private val eventRepository: EventRepository,
    private val teamRepository: TeamRepository,
    private val playerRepository: PlayerRepository,
    private val connectivityManager: ConnectivityManager,
) : ViewModel() {

    private val _countryWithLeagueWithTeamsAndEvents =
        MutableLiveData<Event<Resource<CountryWithLeagueWithEventsAndTeams>>>()
    val countryWithLeagueWithTeamsAndEvents: LiveData<Event<Resource<CountryWithLeagueWithEventsAndTeams>>> =
        _countryWithLeagueWithTeamsAndEvents

    private val eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions =
        MutableLiveData<EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions>()

    private val homeTeamWithPlayersAndCoach = MutableLiveData<TeamWithPlayersAndCoach>()

    private val awayTeamWithPlayersAndCoach = MutableLiveData<TeamWithPlayersAndCoach>()

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange


    private val _switchedDate = MutableLiveData<String>(
        DateUtils.formatDateCurrentDate(
            Constants.DATE_FORMAT_YEAR_MONTH_DAY
        )
    )
    val switchedDate: LiveData<String> = _switchedDate


    val eventsWithDetailsWithHomeAndAwayTeams = ViewModelUtils.zipTripleLiveData(
        homeTeamWithPlayersAndCoach,
        awayTeamWithPlayersAndCoach,
        eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
    )


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
            eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions.postValue(data)

        }
    }

    fun getPlayersAndCoachFromHomeTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = playerRepository.getPlayersFromSpecificTeamFromDb(teamId)
            homeTeamWithPlayersAndCoach.postValue(data)
        }
    }

    fun getPlayersAndCoachFromAwayTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = playerRepository.getPlayersFromSpecificTeamFromDb(teamId)
            awayTeamWithPlayersAndCoach.postValue(data)
        }
    }

    fun switchDate(date: String) {
        viewModelScope.launch {
            _switchedDate.value = date
        }
    }
}