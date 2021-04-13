package com.kuba.flashscore.data.local.daos.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.data.domain.models.event.Statistic
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.util.DataProducer.produceCardEntity
import com.kuba.flashscore.util.DataProducer.produceCountryEntity
import com.kuba.flashscore.util.DataProducer.produceEventEntity
import com.kuba.flashscore.util.DataProducer.produceEventInformationEntity
import com.kuba.flashscore.util.DataProducer.produceGoalscorerEntity
import com.kuba.flashscore.util.DataProducer.produceLeagueEntity
import com.kuba.flashscore.util.DataProducer.produceLineupEntity
import com.kuba.flashscore.util.DataProducer.produceStatisticEntity
import com.kuba.flashscore.util.DataProducer.produceSubstitutionEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class EventDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var eventDao: EventDao
    private lateinit var leagueDao: LeagueDao
    private lateinit var countryDao: CountryDao
    private lateinit var teamDao: TeamDao
    private lateinit var cardDao: CardDao
    private lateinit var goalscorerDao: GoalscorerDao
    private lateinit var statisticDao: StatisticDao
    private lateinit var lineupDao: LineupDao
    private lateinit var substitutionDao: SubstitutionDao


    private lateinit var eventItem1: EventEntity
    private lateinit var eventItem2: EventEntity
    private lateinit var cardItem: CardEntity
    private lateinit var goalscorerItem: GoalscorerEntity
    private lateinit var lineupItem: LineupEntity
    private lateinit var statisticItem: StatisticEntity
    private lateinit var substitutionItem: SubstitutionsEntity
    private lateinit var countryItem: CountryEntity
    private lateinit var leagueItem: LeagueEntity
    private lateinit var teamItem: TeamEntity

    private lateinit var eventInformation1: EventInformationEntity
    private lateinit var eventInformation2: EventInformationEntity
    private lateinit var eventInformation3: EventInformationEntity
    private lateinit var eventInformation4: EventInformationEntity


    @Before
    fun setup() {
        hiltRule.inject()
        eventDao = database.eventDao()
        leagueDao = database.leagueDao()
        countryDao = database.countryDao()
        teamDao = database.teamDao()
        cardDao = database.cardDao()
        goalscorerDao = database.goalscorerDao()
        statisticDao = database.statisticDao()
        lineupDao = database.lineupDao()
        substitutionDao = database.substitutionDao()

        cardItem = produceCardEntity(1, 1, true)
        goalscorerItem = produceGoalscorerEntity(1, 1, true)
        lineupItem = produceLineupEntity(1, 1, true)
        statisticItem = produceStatisticEntity(1, 1)
        substitutionItem = produceSubstitutionEntity(1, 1, true)
        countryItem = produceCountryEntity(1)
        leagueItem = produceLeagueEntity(1, 1)
        teamItem = produceTeamEntity(1, 1)

        eventItem1 = produceEventEntity(1, 1)
        eventItem2 = produceEventEntity(2, 1)

        eventInformation1 = produceEventInformationEntity(1, 1, 1, true)
        eventInformation2 = produceEventInformationEntity(2, 1, 2, false)
        eventInformation3 = produceEventInformationEntity(3, 2, 3, true)
        eventInformation4 = produceEventInformationEntity(4, 2, 4, false)

        runBlockingTest {
            countryDao.insertCountries(listOf(countryItem))
            leagueDao.insertLeagues(listOf(leagueItem))
            teamDao.insertTeam(teamItem)
            cardDao.insertCards(listOf(cardItem))
            goalscorerDao.insertGoalscorers(listOf(goalscorerItem))
            lineupDao.insertLineup(listOf(lineupItem))
            statisticDao.insertStatistics(listOf(statisticItem))
            substitutionDao.insertSubstitutions(listOf(substitutionItem))
            eventDao.insertEventInformation(
                listOf(
                    eventInformation1,
                    eventInformation2,
                    eventInformation3,
                    eventInformation4
                )
            )
            eventDao.insertEvents(listOf(eventItem1, eventItem2))
        }


    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertEventAndEventInformation_getEventsFromSpecificLeague() = runBlockingTest {
        val events = eventDao.getEventsFromSpecificLeague("leagueId1", "matchDate1")

        assertThat(events).isEqualTo(listOf(eventItem1, eventItem2))
    }

    @Test
    fun insertEventAndEventInformation_getCountryWithLeagueWithTeamsAndEvents() = runBlockingTest {
        val countryWithLeagueWithTeamsAndEvents =
            eventDao.getCountryWithLeagueWithTeamsAndEvents("leagueId1", "matchDate1")

        assertThat(countryWithLeagueWithTeamsAndEvents.leagueWithEventEntities).isNotEmpty()
        assertThat(countryWithLeagueWithTeamsAndEvents.leagueWithEventEntities[0].eventsWithEventsInformation).isNotEmpty()
        assertThat(countryWithLeagueWithTeamsAndEvents.leagueWithEventEntities[0].eventsWithEventsInformation[0]?.event).isEqualTo(
            eventItem1
        )
        assertThat(countryWithLeagueWithTeamsAndEvents.leagueWithEventEntities[0].eventsWithEventsInformation[0]?.eventInformation).isEqualTo(
            listOf(eventInformation1, eventInformation2)
        )
    }

    @Test
    fun insertEventAndEventInformation_getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions() =
        runBlockingTest {
            val eventWithDetails =
                eventDao.getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions("matchId1")

            assertThat(eventWithDetails.cards).contains(cardItem)
            assertThat(eventWithDetails.goalscorers).contains(goalscorerItem)
            assertThat(eventWithDetails.lineups).contains(lineupItem)
            assertThat(eventWithDetails.statistics).contains(statisticItem)
            assertThat(eventWithDetails.substitutions).contains(substitutionItem)
            assertThat(eventWithDetails.eventEntity).isEqualTo(eventItem1)
        }
}