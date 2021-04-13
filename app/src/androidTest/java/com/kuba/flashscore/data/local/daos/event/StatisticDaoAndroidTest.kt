package com.kuba.flashscore.data.local.daos.event

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.kuba.flashscore.data.local.daos.CountryDao
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
import com.kuba.flashscore.data.local.models.entities.event.CardEntity
import com.kuba.flashscore.data.local.models.entities.event.GoalscorerEntity
import com.kuba.flashscore.data.local.models.entities.event.StatisticEntity
import com.kuba.flashscore.util.DataProducer.produceStatisticEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class StatisticDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: StatisticDao

    private lateinit var statisticItem1: StatisticEntity
    private lateinit var statisticItem2: StatisticEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.statisticDao()

        statisticItem1 = produceStatisticEntity(1, 1)
        statisticItem2 = produceStatisticEntity(2, 1)

        runBlockingTest {
            dao.insertStatistics(listOf(statisticItem1, statisticItem2))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertStatisticItem_getStatisticsFromSpecificMatch() = runBlockingTest {
        val statistics = dao.getStatisticsFromSpecificMatch("matchId1")

        assertThat(statistics).isEqualTo(listOf(statisticItem1, statisticItem2))
    }
}