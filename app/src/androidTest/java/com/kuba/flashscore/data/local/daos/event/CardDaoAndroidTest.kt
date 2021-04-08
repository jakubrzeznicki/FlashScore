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


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CardDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: CardDao

    private lateinit var cardItem1: CardEntity
    private lateinit var cardItem2: CardEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.cardDao()

        cardItem1 = CardEntity(
            "matchId1",
            "fault1",
            "card1",
            true,
            "info1",
            "time1"
        )

        cardItem2 = CardEntity(
            "matchId1",
            "fault2",
            "card2",
            true,
            "info2",
            "time2"
        )

        runBlockingTest {
            dao.insertCards(listOf(cardItem1, cardItem2))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCardItem_getCardsFromSpecificMatchItems() = runBlockingTest {
        val cards = dao.getCardsFromSpecificMatch("matchId1")

        assertThat(cards).isEqualTo(listOf(cardItem1, cardItem2))
    }
}