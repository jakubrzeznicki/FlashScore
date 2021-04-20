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
import com.kuba.flashscore.data.local.models.entities.event.CardEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceCardEntity


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

        cardItem1 = produceCardEntity(1, 1, true)
        cardItem2 = produceCardEntity(2, 1, true)

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