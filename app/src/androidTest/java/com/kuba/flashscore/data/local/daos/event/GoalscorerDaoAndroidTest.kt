package com.kuba.flashscore.data.local.daos.event

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
import com.kuba.flashscore.data.local.models.entities.event.GoalscorerEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceGoalscorerEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class GoalscorerDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: GoalscorerDao

    private lateinit var goalscorerItem1: GoalscorerEntity
    private lateinit var goalscorerItem2: GoalscorerEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.goalscorerDao()

        goalscorerItem1 = produceGoalscorerEntity(1,1,true)
        goalscorerItem2 = produceGoalscorerEntity(2,1,true)

        runBlockingTest {
            dao.insertGoalscorers(listOf(goalscorerItem1, goalscorerItem2))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertGoalscorerItem_getGoalscorerFromSpecificMatch() = runBlockingTest {
        val goalscorers = dao.getGoalscorersFromSpecificMatch("matchId1")

        assertThat(goalscorers).isEqualTo(listOf(goalscorerItem1, goalscorerItem2))
    }
}