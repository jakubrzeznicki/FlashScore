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
import com.kuba.flashscore.data.local.models.entities.event.LineupEntity
import com.kuba.flashscore.util.DataProducer.produceLineupEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class LineupDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: LineupDao

    private lateinit var lineupItem1: LineupEntity
    private lateinit var lineupItem2: LineupEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.lineupDao()

        lineupItem1 = produceLineupEntity(1, 1, true)
        lineupItem2 = produceLineupEntity(2, 1, true)

        runBlockingTest {
            dao.insertLineup(listOf(lineupItem1, lineupItem2))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLineupItem_getLineupFromSpecificMatch() = runBlockingTest {
        val lineup = dao.getLineupsFromSpecificMatch("matchId1")

        assertThat(lineup).isEqualTo(listOf(lineupItem1, lineupItem2))
    }
}