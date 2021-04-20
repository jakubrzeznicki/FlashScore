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


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class CountryDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: CountryDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.countryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertCountryItem_getAllCountryItems() = runBlockingTest {
        val countryItem = produceCountryEntity(1)
        dao.insertCountries(listOf(countryItem))

        val allCountryItems = dao.getAllCountriesFromDb()

        assertThat(allCountryItems).contains(countryItem)
    }
}