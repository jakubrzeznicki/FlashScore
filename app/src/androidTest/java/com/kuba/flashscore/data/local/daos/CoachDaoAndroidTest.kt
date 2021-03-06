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
import com.kuba.flashscore.data.local.models.entities.CoachEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceCoachEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CoachDaoAndroidTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: CoachDao

    private lateinit var coachItem1: CoachEntity
    private lateinit var coachItem2: CoachEntity
    private lateinit var coachItem3: CoachEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.coachDao()

        coachItem1 = produceCoachEntity(1, 1)
        coachItem2 = produceCoachEntity(2, 2)
        coachItem3 = produceCoachEntity(3, 3)

        runBlockingTest {
            dao.insertCoaches(listOf(coachItem1, coachItem2, coachItem3))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCoachItem_getAllCoachItems() = runBlockingTest {
        val coachItems = dao.getAllCoaches()

        assertThat(coachItems).isEqualTo(listOf(coachItem1, coachItem2, coachItem3))
    }

    @Test
    fun insertCoachItem_getCoachFromSpecificTeam() = runBlockingTest {
        val coachItem = dao.getCoachFromSpecificTeam("teamId1")

        assertThat(coachItem).isEqualTo(coachItem1)
    }
}