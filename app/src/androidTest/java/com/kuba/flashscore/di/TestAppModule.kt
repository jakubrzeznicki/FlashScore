package com.kuba.flashscore.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.daos.event.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.data.network.mappers.*
import com.kuba.flashscore.data.network.mappers.event.EventDtoMapper
import com.kuba.flashscore.repositories.country.CountryRepository
import com.kuba.flashscore.repositories.country.DefaultCountryRepository
import com.kuba.flashscore.repositories.event.DefaultEventRepository
import com.kuba.flashscore.repositories.event.EventRepository
import com.kuba.flashscore.repositories.league.DefaultLeagueRepository
import com.kuba.flashscore.repositories.league.LeagueRepository
import com.kuba.flashscore.repositories.player.DefaultPlayerRepository
import com.kuba.flashscore.repositories.player.PlayerRepository
import com.kuba.flashscore.repositories.standing.DefaultStandingRepository
import com.kuba.flashscore.repositories.standing.StandingRepository
import com.kuba.flashscore.repositories.team.DefaultTeamRepository
import com.kuba.flashscore.repositories.team.TeamRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class TestAPIModule {

    @Provides
    fun giveRetrofitAPIService(): ApiFootballService =
        Retrofit.Builder()
            .baseUrl("http://localhost:8080/")
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(ApiFootballService::class.java)


    @Provides
    @Singleton
    fun provideUrl(): String = "http://localhost:8080/"

    @Singleton
    @Provides
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
    )

    @Singleton
    @Provides
    fun provideLeagueRepository(
        leagueDao: LeagueDao,
        leagueDtoMapper: LeagueDtoMapper,
        api: ApiFootballService
    ) =
        DefaultLeagueRepository(
            leagueDao,
            leagueDtoMapper,
            api
        ) as LeagueRepository

    @Singleton
    @Provides
    fun provideTeamRepository(
        coachDao: CoachDao,
        playerDao: PlayerDao,
        teamDao: TeamDao,
        teamDtoMapper: TeamDtoMapper,
        coachDtoMapper: CoachDtoMapper,
        playerDtoMapper: PlayerDtoMapper,
        api: ApiFootballService
    ) =
        DefaultTeamRepository(
            coachDao,
            playerDao,
            teamDao,
            teamDtoMapper,
            coachDtoMapper,
            playerDtoMapper,
            api
        ) as TeamRepository

    @Singleton
    @Provides
    fun provideStandingRepository(
        standingDao: StandingDao,
        standingDtoMapper: StandingDtoMapper,
        api: ApiFootballService
    ) =
        DefaultStandingRepository(
            standingDao,
            standingDtoMapper,
            api
        ) as StandingRepository

    @Singleton
    @Provides
    fun provideEventRepository(
        cardDao: CardDao,
        goalscorerDao: GoalscorerDao,
        lineupDao: LineupDao,
        statisticDao: StatisticDao,
        substitutionDao: SubstitutionDao,
        eventsDao: EventDao,
        eventDtoMapper: EventDtoMapper,
        api: ApiFootballService
    ) =
        DefaultEventRepository(
            cardDao,
            goalscorerDao,
            lineupDao,
            statisticDao,
            substitutionDao,
            eventsDao,
            eventDtoMapper,
            api
        ) as EventRepository

    @Singleton
    @Provides
    fun provideCountryRepository(
        countryDao: CountryDao,
        countryDtoMapper: CountryDtoMapper,
        api: ApiFootballService
    ) =
        DefaultCountryRepository(
            countryDao,
            countryDtoMapper,
            api
        ) as CountryRepository

    @Singleton
    @Provides
    fun providePlayerRepository(
        playerDao: PlayerDao,
        api: ApiFootballService
    ) =
        DefaultPlayerRepository(
            playerDao,
            api
        ) as PlayerRepository

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

}