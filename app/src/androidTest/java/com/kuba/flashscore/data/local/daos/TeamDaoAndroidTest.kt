package com.kuba.flashscore.data.local.daos

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
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
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.models.entities.*


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class TeamDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var leagueDao: LeagueDao
    private lateinit var countryDao: CountryDao
    private lateinit var playerDao: PlayerDao
    private lateinit var coachDao: CoachDao
    private lateinit var teamDao: TeamDao

    private lateinit var playerItem: PlayerEntity
    private lateinit var coachItem: CoachEntity

    @Before
    fun setup() {
        hiltRule.inject()
        leagueDao = database.leagueDao()
        countryDao = database.countryDao()
        playerDao = database.playerDao()
        coachDao = database.coachDao()
        teamDao = database.teamDao()

        val countryItem = CountryEntity("countryId1", "countryLogo1", "countryName1", 100L)
        val leagueItem =
            LeagueEntity("countryId1", "leagueId1", "leagueLogo1", "leagueName1", "leagueSeason1")

        playerItem = PlayerEntity(
            "teamId1",
            "playerAge1",
            "playerCountry1",
            "playerGoals1",
            1L,
            "playerMatchPlayed1",
            "playerName1",
            "playerNumber1",
            "playerRedCard1",
            "playerType1",
            "playerYellowCard1"
        )
        coachItem = CoachEntity(
            "teamId1",
            "coachAge1",
            "coachCountry1",
            "coachName1"
        )

        runBlockingTest {
            countryDao.insertCountries(listOf(countryItem))
            leagueDao.insertLeagues(listOf(leagueItem))
            playerDao.insertPlayers(listOf(playerItem))
            coachDao.insertCoaches(listOf(coachItem))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertTeamItems_getTeamsFromSpecificLeague() = runBlockingTest {
        val team1 = TeamEntity(
            "leagueId1",
            "teamBadge1",
            "teamId1",
            "teamName1"
        )
        val team2 = TeamEntity(
            "leagueId1",
            "teamBadge2",
            "teamId2",
            "teamName2"
        )

        teamDao.insertTeams(listOf(team1, team2))

        val teamItems = teamDao.getTeamsFromSpecificLeague("leagueId1")

        assertThat(teamItems.leagueWithTeamEntities).isNotEmpty()
        assertThat(teamItems.leagueWithTeamEntities[0].teams).isNotEmpty()
        assertThat(teamItems.leagueWithTeamEntities[0].teams).isEqualTo(listOf(team1, team2))
    }

    @Test
    fun insertTeamItem_getTeamByTeamId() = runBlockingTest {
        val team = TeamEntity(
            "leagueId1",
            "teamBadge1",
            "teamId1",
            "teamName1"
        )

        teamDao.insertTeam(team)

        val teamItem = teamDao.getTeamByTeamId("teamId1")

        assertThat(teamItem.team).isEqualTo(team)
        assertThat(teamItem.players).contains(playerItem)
        assertThat(teamItem.coaches).contains(coachItem)
    }
}