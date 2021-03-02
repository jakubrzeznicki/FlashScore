package com.kuba.flashscore.ui.events

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kuba.flashscore.local.models.entities.CountryWithLeagueAndTeams
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.network.mappers.CoachDtoMapper
import com.kuba.flashscore.network.mappers.PlayerDtoMapper
import com.kuba.flashscore.network.mappers.TeamDtoMapper
import com.kuba.flashscore.network.mappers.event.EventDtoMapper
import com.kuba.flashscore.network.responses.EventResponse
import com.kuba.flashscore.network.responses.TeamResponse
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Event
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.event.EventRepository
import com.kuba.flashscore.repositories.team.TeamRepository
import com.kuba.flashscore.ui.util.ConnectivityManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import timber.log.Timber

class EventsViewModel @ViewModelInject constructor(
    private val eventRepository: EventRepository,
    private val teamRepository: TeamRepository,
    private val connectivityManager: ConnectivityManager,
    private val eventDtoMapper: EventDtoMapper,
    private val teamDtoMapper: TeamDtoMapper,
    private val coachDtoMapper: CoachDtoMapper,
    private val playerDtoMapper: PlayerDtoMapper
) : ViewModel() {

    private val _eventsFromDb = MutableLiveData<Event<Resource<List<EventEntity>>>>()
    val eventsFromDb: LiveData<Event<Resource<List<EventEntity>>>> = _eventsFromDb

    private val _eventsFromNetworkStatus = MutableLiveData<Event<Resource<EventResponse>>>()
    val eventsFromNetworkStatus: LiveData<Event<Resource<EventResponse>>> = _eventsFromNetworkStatus

    private val _teamsFromDb = MutableLiveData<Event<Resource<CountryWithLeagueAndTeams>>>()
    val teamsFromDb: LiveData<Event<Resource<CountryWithLeagueAndTeams>>> = _teamsFromDb

    private val _teamsFromNetworkStatus = MutableLiveData<Event<Resource<TeamResponse>>>()
    val teamsFromNetworkStatus: LiveData<Event<Resource<TeamResponse>>> = _teamsFromNetworkStatus

    private val _networkConnectivityChange = connectivityManager.isNetworkAvailable
    val networkConnectivityChange: LiveData<Boolean> = _networkConnectivityChange


    fun getEventsFromSpecificLeague(leagueId: String, from: String, to: String) {
        _eventsFromNetworkStatus.value = Event(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                Timber.d("JUREKKK in viewmodelsdfsd")

                if (isNetworkAvailable) {
                    val response =
                        eventRepository.getEventsFromSpecificLeaguesFromNetwork(leagueId, from, to)
                    //_eventsFromNetworkStatus.postValue(Event(response))
                    _eventsFromDb.value = Event(response)
                    Timber.d("JUREKKK in viewmodel ${response.status}")
                    Timber.d("JUREKKK in viewmodel ${response.data}")
//                    if (response.status == Status.SUCCESS) {
//                        eventRepository.apply {
//                            insertEvents(
//                                eventDtoMapper.toLocalList(
//                                    response.data?.toList()!!,
//                                    null
//                                )
//                            )
//
//                            response.data.toList().forEach { eventDtoItem ->
//                                if (eventDtoItem.cards.isNotEmpty()) {
//                                    async {
//                                        insertCards(
//                                            eventDtoMapper.mapCardToLocal(
//                                                eventDtoItem.cards,
//                                                eventDtoItem.matchId
//                                            )
//                                        )
//                                    }
//                                }
//
//                                if (eventDtoItem.goalscorer.isNotEmpty()) {
//                                    async {
//                                        insertGoalscorers(
//                                            eventDtoMapper.mapGoalscorerToLocal(
//                                                eventDtoItem.goalscorer,
//                                                eventDtoItem.matchId
//                                            )
//                                        )
//                                    }
//                                }
//                                if (eventDtoItem.statistics.isNotEmpty()) {
//                                    async {
//                                        insertStatistics(
//                                            eventDtoMapper.mapStatisticToLocal(
//                                                eventDtoItem.statistics,
//                                                eventDtoItem.matchId
//                                            )
//                                        )
//                                    }
//                                }
//                                if (eventDtoItem.substitutions.away.isNotEmpty() || eventDtoItem.substitutions.home.isNotEmpty()) {
//                                    async {
//                                        insertSubstitutions(
//                                            eventDtoMapper.mapSubstitutionsToLocal(
//                                                eventDtoItem.substitutions,
//                                                eventDtoItem.matchId
//                                            )
//                                        )
//                                    }
//                                }
//                                async {
//                                    insertLineups(
//                                        eventDtoMapper.mapLineupToLocal(
//                                            eventDtoItem.lineup,
//                                            eventDtoItem.matchId
//                                        )
//                                    )
//                                }
//                            }
//
//                        }
//                        _eventsFromDb.value =
//                            eventRepository.getEventsFromSpecificLeaguesFromDb(
//                                leagueId,
//                                from
//                            )
//                        Timber.d(
//                            "JUREKKKKK gettt ${
//                                eventRepository.getEventsFromSpecificLeaguesFromDb(
//                                    leagueId,
//                                    from
//                                )
//                            }"
//                        )
//                    }

                } else {
                    val response =
                        eventRepository.getEventsFromSpecificLeaguesFromDb(
                            leagueId, from
                        )
                    if (response == null) {
                        Timber.d("JUREK fetch teams form db coun erro")
                        _eventsFromNetworkStatus.value =
                            Event(Resource.error("A lack of data", null))
                    } else {
                        _eventsFromDb.value = Event(Resource.success(response))
                    }
                }
            }
        }
    }

    fun getTeamsFormSpecificLeague(leagueId: String) {
        _teamsFromNetworkStatus.value = Event(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val response =
                        teamRepository.getTeamsFromSpecificLeagueFromNetwork(leagueId)
                    _teamsFromDb.value = Event(response)
//                    _teamsFromNetworkStatus.postValue(Event(response))
//                    if (response.status == Status.SUCCESS) {
//                        teamRepository.apply {
//                            insertTeams(
//                                teamDtoMapper.toLocalList(
//                                    response.data?.toList()!!,
//                                    leagueId
//                                )
//                            )
//
//                            response.data.toList().forEach { teamDtoItem ->
//                                insertPlayers(
//                                    playerDtoMapper.toLocalList(
//                                        teamDtoItem.players,
//                                        teamDtoItem.teamKey
//                                    )
//                                )
//
//                                insertCoaches(
//                                    coachDtoMapper.toLocalList(
//                                        teamDtoItem.coaches,
//                                        teamDtoItem.teamKey
//                                    )
//                                )
//                            }
//                        }
//                        _teamsFromDb.value = teamRepository.getTeamsFromLeagueFromDb(leagueId)
//                        Timber.d(
//                            "JUREKKKKK gettt ${
//                                teamRepository.getTeamsFromLeagueFromDb(
//                                    leagueId
//                                )
//                            }"
//                        )
//
//                    }
                } else {
                    val response =
                        teamRepository.getTeamsFromLeagueFromDb(
                            leagueId
                        )
                    if (response == null) {
                        Timber.d("JUREK fetch teams form db coun erro")
                        _eventsFromNetworkStatus.value =
                            Event(Resource.error("A lack of data", null))
                    } else {
                        _teamsFromDb.value = Event(Resource.success(response!!))
                    }
                }
            }
        }
    }
}