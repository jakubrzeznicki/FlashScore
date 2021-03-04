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

    private val _eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions =
        MutableLiveData<EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions>()
    val eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions:
            LiveData<EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions> =
        _eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions

    private val _homeTeamWithPlayersAndCoach = MutableLiveData<TeamWithPlayersAndCoach>()
    val homeTeamWithPlayersAndCoach: LiveData<TeamWithPlayersAndCoach> =
        _homeTeamWithPlayersAndCoach

    private val _awayTeamWithPlayersAndCoach = MutableLiveData<TeamWithPlayersAndCoach>()
    val awayTeamWithPlayersAndCoach: LiveData<TeamWithPlayersAndCoach> =
        _awayTeamWithPlayersAndCoach

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange


    private val _switchedDate = MutableLiveData<String>(
        DateUtils.formatDateCurrentDate(
            Constants.DATE_FORMAT_YEAR_MONTH_DAY
        )
    )
    val switchedDate: LiveData<String> = _switchedDate


    val eventsWithDetailsWithHomeAndAwayTeams = zipLiveData(
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

    fun getPlayersAndCoachFromHomeTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = playerRepository.getPlayersFromSpecificTeamFromDb(teamId)
            _homeTeamWithPlayersAndCoach.postValue(data)
        }
    }

    fun getPlayersAndCoachFromAwayTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data = playerRepository.getPlayersFromSpecificTeamFromDb(teamId)
            _awayTeamWithPlayersAndCoach.postValue(data)
        }
    }

    fun switchDate(date: String) {
        viewModelScope.launch {
            _switchedDate.value = date
        }
    }

    private fun <A, B, C> zipLiveData(
        a: LiveData<A>,
        b: LiveData<B>,
        c: LiveData<C>
    ): LiveData<Triple<A, B, C>> {
        return MediatorLiveData<Triple<A, B, C>>().apply {
            var lastA: A? = null
            var lastB: B? = null
            var lastC: C? = null

            fun update() {
                val localLastA = lastA
                val localLastB = lastB
                val localLastC = lastC
                if (localLastA != null && localLastB != null && localLastC != null)
                    this.value = Triple(localLastA, localLastB, localLastC)
            }

            addSource(a) {
                lastA = it
                update()
            }
            addSource(b) {
                lastB = it
                update()
            }
            addSource(c) {
                lastC = it
                update()
            }
        }
    }
}