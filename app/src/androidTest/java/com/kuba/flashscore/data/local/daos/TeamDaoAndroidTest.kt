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
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.util.DataProducerAndroid.produceCoachEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity
import com.kuba.flashscore.util.DataProducerAndroid.producePlayerEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceTeamEntity


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

        val countryItem = produceCountryEntity(1)
        val leagueItem = produceLeagueEntity(1, 1)
        playerItem = producePlayerEntity(1, 1)
        coachItem = produceCoachEntity(1, 1)

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
        val team1 = produceTeamEntity(1, 1)
        val team2 = produceTeamEntity(2, 1)

        teamDao.insertTeams(listOf(team1, team2))

        val teamItems = teamDao.getTeamsFromSpecificLeague("leagueId1")

        assertThat(teamItems.leagueWithTeamEntities).isNotEmpty()
        assertThat(teamItems.leagueWithTeamEntities[0].teams).isNotEmpty()
        assertThat(teamItems.leagueWithTeamEntities[0].teams).isEqualTo(listOf(team1, team2))
    }

    @Test
    fun insertTeamItem_getTeamByTeamId() = runBlockingTest {
        val team = produceTeamEntity(1, 1)
        teamDao.insertTeam(team)

        val teamItem = teamDao.getTeamByTeamId("teamId1")

        assertThat(teamItem.team).isEqualTo(team)
        assertThat(teamItem.players).contains(playerItem)
        assertThat(teamItem.coaches).contains(coachItem)
    }
}