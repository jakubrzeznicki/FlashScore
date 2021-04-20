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
import com.kuba.flashscore.util.DataProducerAndroid.produceCountryEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceLeagueEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class LeagueDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var leagueDao: LeagueDao
    private lateinit var countryDao: CountryDao

    @Before
    fun setup() {
        hiltRule.inject()
        leagueDao = database.leagueDao()
        countryDao = database.countryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertLeagueItem_getAllLeagueItems() = runBlockingTest {
        val leagueItem = produceLeagueEntity(1, 1)
        leagueDao.insertLeagues(listOf(leagueItem))

        val allLeagueItems = leagueDao.getAllLeagues()

        assertThat(allLeagueItems).contains(leagueItem)
    }

    @Test
    fun insertLeagueItem_getLeaguesFromSpecificCountry() = runBlockingTest {
        val countryItem = produceCountryEntity(1)
        countryDao.insertCountries(listOf(countryItem))

        val leagueItem1 = produceLeagueEntity(1, 1)

        val leagueItem2 = produceLeagueEntity(2, 2)

        leagueDao.insertLeagues(listOf(leagueItem1, leagueItem2))

        val leagueItems = leagueDao.getLeaguesFromSpecificCountry("countryId1")

        assertThat(leagueItems.leagues).contains(leagueItem1)
    }
}