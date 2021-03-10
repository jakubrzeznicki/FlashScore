package com.kuba.flashscore.ui.events

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.data.domain.models.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.other.*
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE
import com.kuba.flashscore.other.Constants.ERROR_MESSAGE_LACK_OF_DATA
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

    private val _countryWithLeagueWithTeamsAndEventsStatus =
        MutableLiveData<Event<Resource<List<com.kuba.flashscore.data.domain.models.event.Event>>>>()
    val countryWithLeagueWithTeamsAndEventsEntityStatus: LiveData<Event<Resource<List<com.kuba.flashscore.data.domain.models.event.Event>>>> =
        _countryWithLeagueWithTeamsAndEventsStatus

    private val _countryWithLeagueWithTeamsAndEvents =
        MutableLiveData<CountryWithLeagueWithEventsAndTeams>()
    val countryWithLeagueWithTeamsAndEvents: LiveData<CountryWithLeagueWithEventsAndTeams> =
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


    suspend fun refreshEvents(leagueId: String, date: String) {
        _countryWithLeagueWithTeamsAndEventsStatus.value = Event(Resource.loading(null))
        viewModelScope.launch(Dispatchers.IO) {
            connectivityManager.isNetworkAvailable.value!!.let { isNetworkAvailable ->
                if (isNetworkAvailable) {
                    val eventResponse =
                        eventRepository.refreshEventsFromSpecificLeagues(leagueId, date)
                    val teamsResponse =
                        teamRepository.refreshTeamsFromSpecificLeague(leagueId)

                    if (eventResponse.status == Status.SUCCESS && teamsResponse.status == Status.SUCCESS) {
                        _countryWithLeagueWithTeamsAndEventsStatus.postValue(
                            Event(eventResponse)
                        )
                    } else {
                        _countryWithLeagueWithTeamsAndEventsStatus.postValue(
                            Event(
                                Resource.error(
                                    eventResponse.message ?: teamsResponse.message
                                    ?: ERROR_MESSAGE_LACK_OF_DATA, null
                                )
                            )
                        )
                    }
                } else {
                    Event(Resource.error(ERROR_MESSAGE, null))
                }
            }
        }

    }

    fun getCountryWithLeagueWithEventsAndTeams(leagueId: String, date: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _countryWithLeagueWithTeamsAndEvents.postValue(
                eventRepository.getCountryWithLeagueWithTeamsAndEvents(
                    leagueId,
                    date
                )?.asDomainModel()
            )
        }
    }

    fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(eventId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val data =
                eventRepository.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
                    eventId
                ).asDomainModel()
            Timber.d("EVENT DETAILS IN VIEW MODEL ${data.event.matchRound}")
            eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions.postValue(
                data
            )
        }
    }

    fun getPlayersAndCoachFromHomeTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = playerRepository.getPlayersFromSpecificTeamFromDb(
                teamId
            ).asDomainModel()
            Timber.d("EVENT DETAILS team home IN VIEW MODEL ${date.team.teamName}")
            homeTeamWithPlayersAndCoach.postValue(
                date
            )
        }
    }

    fun getPlayersAndCoachFromAwayTeam(teamId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val date = playerRepository.getPlayersFromSpecificTeamFromDb(
                teamId
            ).asDomainModel()
            Timber.d("EVENT DETAILS team away IN VIEW MODEL ${date.team.teamName}")
            awayTeamWithPlayersAndCoach.postValue(
                date
            )
        }
    }

    fun switchDate(date: String) {
        viewModelScope.launch {
            _switchedDate.value = date
        }
    }
}