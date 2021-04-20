package com.kuba.flashscore.di

import android.content.Context
import androidx.room.Room
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.repositories.country.FakeCountryRepositoryAndroidTest
import com.kuba.flashscore.util.DataProducerAndroid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object TestAppModule {

    @Provides
    @Named("test_db")
    fun provideInMemoryDb(
        @ApplicationContext context: Context
    ) = Room.inMemoryDatabaseBuilder(context, FlashScoreDatabase::class.java)
        .allowMainThreadQueries()
        .build()

    @Provides
    fun provideFakCountryRepository(): FakeCountryRepositoryAndroidTest {
        return FakeCountryRepositoryAndroidTest()
    }

    @Singleton
    @Provides
    fun provideCountryWithLeagueAndTeams() : CountryWithLeagueAndTeams {
        return CountryWithLeagueAndTeamsEntity(
            DataProducerAndroid.produceCountryEntity(1),
            listOf(
                LeagueWithTeamsEntity(
                    DataProducerAndroid.produceLeagueEntity(1, 1),
                    listOf(
                        DataProducerAndroid.produceTeamEntity(1, 1),
                        DataProducerAndroid.produceTeamEntity(2, 1)
                    )
                )
            )
        ).asDomainModel()
    }
}