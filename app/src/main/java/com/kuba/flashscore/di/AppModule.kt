package com.kuba.flashscore.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.FlashScoreDatabase
import com.kuba.flashscore.data.local.daos.CountryDao
import com.kuba.flashscore.network.ApiFootballService
import com.kuba.flashscore.network.mappers.*
import com.kuba.flashscore.other.Constants.BASE_URL
import com.kuba.flashscore.other.Constants.DATABASE_NAME
import com.kuba.flashscore.repositories.DefaultFlashScoreRepository
import com.kuba.flashscore.repositories.FlashScoreRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideFlashScoreDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, FlashScoreDatabase::class.java, DATABASE_NAME).build()


    @Singleton
    @Provides
    fun provideCountryMapper(): CountryDtoMapper = CountryDtoMapper()

    @Singleton
    @Provides
    fun provideLeagueMapper(): LeagueDtoMapper = LeagueDtoMapper()


    @Singleton
    @Provides
    fun provideTeamMapper(): TeamDtoMapper = TeamDtoMapper()

    @Singleton
    @Provides
    fun providePlayerMapper(): PlayerDtoMapper = PlayerDtoMapper()

    @Singleton
    @Provides
    fun provideCoacheMapper(): CoacheDtoMapper = CoacheDtoMapper()

    @Singleton
    @Provides
    fun provideStandingMapper(): StandingDtoMapper = StandingDtoMapper()

    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        dao: CountryDao,
        api: ApiFootballService,
        countryMapper: CountryDtoMapper,
        leagueMapper: LeagueDtoMapper,
        teamMapper: TeamDtoMapper,
        standingMapper: StandingDtoMapper
    ) = DefaultFlashScoreRepository(
        dao,
        api,
        countryMapper,
        leagueMapper,
        teamMapper,
        standingMapper
    ) as FlashScoreRepository

    @Singleton
    @Provides
    fun provideCountryDao(
        database: FlashScoreDatabase
    ) = database.countryDao()


    @Singleton
    @Provides
    fun provideApiFootballService(): ApiFootballService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiFootballService::class.java)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
    )
}