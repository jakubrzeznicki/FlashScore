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

        goalscorerItem1 = GoalscorerEntity(
            "matchId1",
            "assistId1",
            "scorerId1",
            true,
            "info1",
            "score1",
            "time1"
        )

        goalscorerItem2 = GoalscorerEntity(
            "matchId1",
            "assistId2",
            "scorerId2",
            true,
            "info2",
            "score2",
            "time2"
        )


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
        val cards = dao.getCardsFromSpecificMatch("matchId1")

        assertThat(cards).isEqualTo(listOf(cardItem1, cardItem2))
    }
}