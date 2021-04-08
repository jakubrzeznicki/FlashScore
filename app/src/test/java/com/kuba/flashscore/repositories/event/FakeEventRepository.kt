package com.kuba.flashscore.repositories.event

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import com.kuba.flashscore.other.Resource

class FakeEventRepository : EventRepository {

    private val cardItems = mutableListOf<CardEntity>()
    private val goalscorerItems = mutableListOf<GoalscorerEntity>()
    private val lineupItems = mutableListOf<LineupEntity>()
    private val statisticsItems = mutableListOf<StatisticEntity>()
    private val substitutionsItems = mutableListOf<SubstitutionsEntity>()
    private val eventItems = mutableListOf<EventEntity>()
    private val eventInformationItems = mutableListOf<EventInformationEntity>()
    private val countryWithLeagueWithEventsAndTeamsItems =
        produceCountryWithLeagueWithEventsAndTeamsEntity()

    private val eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsItems =
        produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity()


    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }


    override suspend fun insertCards(cards: List<CardEntity>) {
        cardItems.addAll(cards)
    }

    override suspend fun insertGoalscorers(goalscorers: List<GoalscorerEntity>) {
        goalscorerItems.addAll(goalscorers)
    }

    override suspend fun insertLineups(lineups: List<LineupEntity>) {
        lineupItems.addAll(lineups)
    }

    override suspend fun insertStatistics(statistics: List<StatisticEntity>) {
        statisticsItems.addAll(statistics)
    }

    override suspend fun insertSubstitutions(substitutions: List<SubstitutionsEntity>) {
        substitutionsItems.addAll(substitutions)
    }

    override suspend fun insertEvents(events: List<EventEntity>) {
        eventItems.addAll(events)
    }

    override suspend fun insertEventInformation(eventInformation: List<EventInformationEntity>) {
        eventInformationItems.addAll(eventInformation)
    }

    override suspend fun getEventsFromSpecificLeaguesFromDb(
        leagueId: String,
        date: String
    ): List<EventEntity> {
        return eventItems
    }

    override suspend fun refreshEventsFromSpecificLeagues(
        leagueId: String,
        date: String
    ): Resource<List<Event>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(eventItems.map { it.asDomainModel() })
        }
    }

    override suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeamsEntity {
        return countryWithLeagueWithEventsAndTeamsItems
    }

    override suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return eventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsItems
    }


//    private fun produceEventEntity(): EventEntity {
//        return EventEntity(
//            "leagueId1",
//            "matchDate1",
//            "matchId1",
//            "matchLive1",
//            "matchReferee1",
//            "matchRound1",
//            "matchStadium1",
//            "matchStatus1",
//            "matchTime1"
//        )
//    }
//
//    fun produceEventInformationHomeEntity(): EventInformationEntity {
//        return EventInformationEntity(
//            "matchId1",
//            "extraScore1",
//            "fullTimeScore1",
//            "halfTimeScore1",
//            "teamId1",
//            "teamPenaltyScore1",
//            "teamScore1",
//            "teamSystem1",
//            true
//        )
//    }
//
//
//    fun produceEventInformationAwayEntity(): EventInformationEntity {
//        return EventInformationEntity(
//            "matchId1",
//            "extraScore2",
//            "fullTimeScore2",
//            "halfTimeScore2",
//            "teamId2",
//            "teamPenaltyScore2",
//            "teamScore2",
//            "teamSystem2",
//            false
//        )
//    }
//
//    fun produceCardEntity(): CardEntity {
//        return CardEntity(
//            "matchId1",
//            "fault1",
//            "card1",
//            true,
//            "info1",
//            "time1"
//        )
//    }
//
//    fun produceGoalscorerEntity(): GoalscorerEntity {
//        return GoalscorerEntity(
//            "matchId1",
//            "assistId1",
//            "scorerId1",
//            true,
//            "info1",
//            "score1",
//            "time1"
//        )
//    }
//
//    fun produceStatisticEntity(): StatisticEntity {
//        return StatisticEntity(
//            "matchId1",
//            "away1",
//            "home1",
//            "type1"
//        )
//    }
//
//    fun produceSubstitutionEntity(): SubstitutionsEntity {
//        return SubstitutionsEntity(
//            "matchId1",
//            "substitutions1",
//            "time1",
//            true
//        )
//    }
//
//    fun produceLineupEntity(): LineupEntity {
//        return LineupEntity(
//            "matchId1",
//            "lineupNumber1",
//            "lineupPosition1",
//            true,
//            true,
//            false,
//            "playerKey1"
//        )
//    }

    fun produceCountryWithLeagueWithEventsAndTeamsEntity(): CountryWithLeagueWithEventsAndTeamsEntity {
        return CountryWithLeagueWithEventsAndTeamsEntity(
            CountryEntity("countryId1", "countryLogo1", "countryName1"),
            listOf(
                LeagueWithTeamsEntity(
                    LeagueEntity(
                        "countryId1",
                        "leagueId1",
                        "leagueLogo1",
                        "leagueName1",
                        "leagueSeason1"
                    ),
                    listOf(
                        TeamEntity(
                            "leagueId1",
                            "teamBadge1",
                            "teamId1",
                            "teamName1",

                            )
                    )
                )
            ),
            listOf(
                LeagueWithEventsEntity(
                    LeagueEntity(
                        "countryId1",
                        "leagueId1",
                        "leagueLogo1",
                        "leagueName1",
                        "leagueSeason1"
                    ),
                    listOf(
                        EventWithEventInformationEntity(
                            EventEntity(
                                "leagueId1",
                                "matchDate1",
                                "matchId1",
                                "matchLive1",
                                "matchReferee1",
                                "matchRound1",
                                "matchStadium1",
                                "matchStatus1",
                                "matchTime1"
                            ),
                            listOf(
                                EventInformationEntity(
                                    "matchId1",
                                    "extraScore1",
                                    "fullTimeScore1",
                                    "halfTimeScore1",
                                    "teamId1",
                                    "teamPenaltyScore1",
                                    "teamScore1",
                                    "teamSystem1",
                                    true
                                ),
                                EventInformationEntity(
                                    "matchId1",
                                    "extraScore2",
                                    "fullTimeScore2",
                                    "halfTimeScore2",
                                    "teamId2",
                                    "teamPenaltyScore2",
                                    "teamScore2",
                                    "teamSystem2",
                                    false
                                )
                            )
                        )
                    )
                )
            )
        )
    }

    fun produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
            EventEntity(
                "leagueId1",
                "matchDate1",
                "matchId1",
                "matchLive1",
                "matchReferee1",
                "matchRound1",
                "matchStadium1",
                "matchStatus1",
                "matchTime1"
            ),
            cardItems,
            goalscorerItems,
            lineupItems,
            statisticsItems,
            substitutionsItems
        )
    }
}
