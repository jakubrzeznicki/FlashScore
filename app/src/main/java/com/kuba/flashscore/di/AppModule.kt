package com.kuba.flashscore.di

import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuba.flashscore.R
import com.kuba.flashscore.local.*
import com.kuba.flashscore.local.database.FlashScoreDatabase
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
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@InstallIn(ApplicationComponent::class)
@Module
object AppModule {

    @Singleton
    @Provides
    fun provideFlashScoreDatabase(
        @ApplicationContext context: Context

    ) = Room.databaseBuilder(context, FlashScoreDatabase::class.java, DATABASE_NAME)
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideCountryDao(
        database: FlashScoreDatabase
    ) = database.countryDao()

    @Singleton
    @Provides
    fun provideLeagueDao(
        database: FlashScoreDatabase
    ) = database.leagueDao()

    @Singleton
    @Provides
    fun provideCoachDao(
        database: FlashScoreDatabase
    ) = database.coachDao()

    @Singleton
    @Provides
    fun providePlayerDao(
        database: FlashScoreDatabase
    ) = database.playerDao()

    @Singleton
    @Provides
    fun provideTeamDao(
        database: FlashScoreDatabase
    ) = database.teamDao()

    @Singleton
    @Provides
    fun provideStandingDao(
        database: FlashScoreDatabase
    ) = database.standingDao()


    @Singleton
    @Provides
    fun provideDefaultShoppingRepository(
        countryDao: CountryDao,
        leagueDao: LeagueDao,
        coachDao: CoachDao,
        playerDao: PlayerDao,
        teamDao: TeamDao,
        standingDao: StandingDao,
        api: ApiFootballService
    ) =
        DefaultFlashScoreRepository(
            countryDao,
            leagueDao,
            coachDao,
            playerDao,
            teamDao,
            standingDao,
            api
        ) as FlashScoreRepository

    @Singleton
    @Provides
    fun provideApiFootballService(): ApiFootballService {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ApiFootballService::class.java)
    }


    @Singleton
    @Provides
    fun provideCountryDtoMapper(): CountryDtoMapper {
        return CountryDtoMapper()
    }

    @Singleton
    @Provides
    fun provideLeagueDtoMapper(): LeagueDtoMapper {
        return LeagueDtoMapper()
    }

    @Singleton
    @Provides
    fun provideCoachDtoMapper(): CoachDtoMapper {
        return CoachDtoMapper()
    }

    @Singleton
    @Provides
    fun providePlayerDtoMapper(): PlayerDtoMapper {
        return PlayerDtoMapper()
    }

    @Singleton
    @Provides
    fun provideTeamDtoMapper(): TeamDtoMapper {
        return TeamDtoMapper()
    }

    @Singleton
    @Provides
    fun provideStandingDtoMapper(): StandingDtoMapper {
        return StandingDtoMapper()
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