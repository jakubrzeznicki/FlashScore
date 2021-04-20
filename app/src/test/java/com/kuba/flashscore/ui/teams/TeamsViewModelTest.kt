package com.kuba.flashscore.ui.teams

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import com.kuba.flashscore.MainCoroutineRule
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.getOrAwaitValueTest
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.other.Status
import com.kuba.flashscore.repositories.country.FakeCountryRepository
import com.kuba.flashscore.repositories.league.FakeLeagueRepository
import com.kuba.flashscore.repositories.team.FakeTeamRepository
import com.kuba.flashscore.ui.util.networking.FakeConnectivityManager
import com.kuba.flashscore.util.DataProducer.produceCoachEntity
import com.kuba.flashscore.util.DataProducer.producePlayerEntity
import com.kuba.flashscore.util.DataProducer.produceTeamEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.lang.RuntimeException

@RunWith(AndroidJUnit4::class)
@ExperimentalCoroutinesApi
@Config(sdk = [Build.VERSION_CODES.O_MR1])
class TeamsViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: TeamsViewModel
    lateinit var teamsRepository: FakeTeamRepository

    @Before
    fun setup() {
        teamsRepository = FakeTeamRepository()

        val team1 = produceTeamEntity(1, 1)
        val team2 = produceTeamEntity(2, 1)
        val team3 = produceTeamEntity(3, 1)

        runBlockingTest {
            teamsRepository.apply {
                insertTeams(listOf(team1, team2, team3))
                insertPlayers(listOf(producePlayerEntity(1, 2)))
                insertCoaches(listOf(produceCoachEntity(1, 2)))
            }
        }
        viewModel = TeamsViewModel(teamsRepository)
    }


    @Test
    fun `get team with coaches and players from database, return success`() {
        viewModel.getTeamWithPlayersAndCoach("teamId2")

        val value = viewModel.team.getOrAwaitValueTest()

        assertThat(value.coaches).isNotEmpty()
        assertThat(value.coaches).hasSize(1)
        assertThat(value.players).isNotEmpty()
        assertThat(value.players).hasSize(1)
        assertThat(value.team.teamKey).isEqualTo("teamId2")
    }

    @Test
    fun `get teams from specific league from database, return success`() {
        viewModel.getCountryWithLeagueAndTeams("leagueId1")

        val value = viewModel.teams.getOrAwaitValueTest()

        assertThat(value.leagueWithTeams).isNotEmpty()
        assertThat(value.leagueWithTeams[0].teams).hasSize(3)
    }
}