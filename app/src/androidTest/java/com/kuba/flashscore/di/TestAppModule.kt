package com.kuba.flashscore.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
import com.kuba.flashscore.repositories.FakeCountryRepositoryAndroidTest
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Named

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
}