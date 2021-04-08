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
class PlayerDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase

    private lateinit var playerDao: PlayerDao
    private lateinit var coachDao: CoachDao
    private lateinit var teamDao: TeamDao

    private lateinit var playerItem1: PlayerEntity
    private lateinit var playerItem2: PlayerEntity

    @Before
    fun setup() {
        hiltRule.inject()

        playerDao = database.playerDao()
        coachDao = database.coachDao()
        teamDao = database.teamDao()

        playerItem1 = PlayerEntity(
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

        playerItem2 = PlayerEntity(
            "teamId1",
            "playerAge2",
            "playerCountry2",
            "playerGoals2",
            2L,
            "playerMatchPlayed2",
            "playerName2",
            "playerNumber2",
            "playerRedCard2",
            "playerType2",
            "playerYellowCard2"
        )

        val coachItem = CoachEntity(
            "teamId1",
            "coachAge1",
            "coachCountry1",
            "coachName1"
        )

        val teamItem = TeamEntity(
            "leagueId1",
            "teamBadge1",
            "teamId1",
            "teamName1"
        )
        runBlockingTest {
            playerDao.insertPlayers(listOf(playerItem1, playerItem2))
            coachDao.insertCoaches(listOf(coachItem))
            teamDao.insertTeam(teamItem)
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertPlayerItems_getAllPlayers() = runBlockingTest {
        val playerItems = playerDao.getAllPlayers()

        assertThat(playerItems).isEqualTo(listOf(playerItem1, playerItem2))
    }


    @Test
    fun insertPlayerItems_getPlayerByPlayerId() = runBlockingTest {
        val playerItem = playerDao.getPlayersByPlayerId(1L)

        assertThat(playerItem).isEqualTo(playerItem1)
    }

    @Test
    fun insertPlayerItems_getPlayerFromSpecificTeam() = runBlockingTest {
        val playerItem = playerDao.getPlayersFromSpecificTeam("teamId1")

        assertThat(playerItem.players).isEqualTo(listOf(playerItem1, playerItem2))
    }

}