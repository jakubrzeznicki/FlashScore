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
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.data.local.daos.LeagueDao
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity


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
        val leagueItem =
            LeagueEntity("countryId1", "leagueId1", "leagueLogo1", "leagueName1", "leagueSeason1")
        leagueDao.insertLeagues(listOf(leagueItem))

        val allLeagueItems = leagueDao.getAllLeagues()

        assertThat(allLeagueItems).contains(leagueItem)
    }

    @Test
    fun insertLeagueItem_getLeaguesFromSpecificCountry() = runBlockingTest {
        val countryItem = CountryEntity("countryId1", "countryLogo1", "countryName1", 100L)
        countryDao.insertCountries(listOf(countryItem))

        val leagueItem1 =
            LeagueEntity("countryId1", "leagueId1", "leagueLogo1", "leagueName1", "leagueSeason1")

        val leagueItem2 =
            LeagueEntity("countryId2", "leagueId2", "leagueLogo2", "leagueName2", "leagueSeason2")

        leagueDao.insertLeagues(listOf(leagueItem1, leagueItem2))

        val leagueItems = leagueDao.getLeaguesFromSpecificCountry("countryId1")

        assertThat(leagueItems.leagues).contains(leagueItem1)
    }
}