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
import com.kuba.flashscore.data.local.models.entities.event.SubstitutionsEntity
import com.kuba.flashscore.util.DataProducerAndroid.produceSubstitutionEntity


@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class SubstitutionDaoAndroidTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlashScoreDatabase
    private lateinit var dao: SubstitutionDao

    private lateinit var substitutionItem1: SubstitutionsEntity
    private lateinit var substitutionItem2: SubstitutionsEntity

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.substitutionDao()

        substitutionItem1 = produceSubstitutionEntity(1, 1, true)
        substitutionItem2 = produceSubstitutionEntity(2, 1, true)

        runBlockingTest {
            dao.insertSubstitutions(listOf(substitutionItem1, substitutionItem2))
        }
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertSubstitutionsItem_getSubstitutionsFromSpecificMatch() = runBlockingTest {
        val substitutions = dao.getSubstitutionsFromSpecificMatch("matchId1")

        assertThat(substitutions).isEqualTo(listOf(substitutionItem1, substitutionItem2))
    }
}