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
import com.kuba.flashscore.util.DataProducerAndroid.produceStandingEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class StandingDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var standingDao: StandingDao

    private lateinit var standing1: StandingEntity
    private lateinit var standing2: StandingEntity
    private lateinit var standing3: StandingEntity

    @Before
    fun setup() {
        hiltRule.inject()
        standingDao = database.standingDao()

        standing1 = produceStandingEntity(1, 1, 1, StandingType.OVERALL)
        standing2 = produceStandingEntity(2, 1, 1, StandingType.HOME)
        standing3 = produceStandingEntity(3, 1, 2, StandingType.OVERALL)

        runBlockingTest {
            standingDao.insertStandings(listOf(standing1, standing2, standing3))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertStandingItems_getStandingsFromSpecificLeague() = runBlockingTest {
        val standingItems = standingDao.getAllStandingsFromSpecificLeague("leagueId1")

        assertThat(standingItems).isNotEmpty()
        assertThat(standingItems).hasSize(3)
        assertThat(standingItems).isEqualTo(listOf(standing1, standing2, standing3))
    }

    @Test
    fun insertStandingItems_getSpecificStandingFromSpecificLeague() = runBlockingTest {

        val standingItems =
            standingDao.getStandingsFromSpecificLeague("leagueId1", StandingType.OVERALL)

        assertThat(standingItems).isNotEmpty()
        assertThat(standingItems).hasSize(2)
        assertThat(standingItems).isEqualTo(listOf(standing1, standing3))
    }
}