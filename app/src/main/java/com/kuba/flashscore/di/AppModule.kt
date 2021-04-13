package com.kuba.flashscore.di

 import android.app.Application
import android.content.Context
import androidx.room.Room
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.kuba.flashscore.R
import com.kuba.flashscore.data.local.daos.*
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
import com.kuba.flashscore.data.local.daos.event.*
import com.kuba.flashscore.data.network.ApiFootballService
import com.kuba.flashscore.other.Constants.BASE_URL
import com.kuba.flashscore.other.Constants.DATABASE_NAME
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
import com.kuba.flashscore.ui.util.networking.ConnectivityManager
import com.kuba.flashscore.ui.util.networking.DefaultConnectivityManager
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
        //.allowMainThreadQueries()
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
    fun provideCardDao(
        database: FlashScoreDatabase
    ) = database.cardDao()


    @Singleton
    @Provides
    fun provideGoalscorerDao(
        database: FlashScoreDatabase
    ) = database.goalscorerDao()


    @Singleton
    @Provides
    fun provideLineupDao(
        database: FlashScoreDatabase
    ) = database.lineupDao()


    @Singleton
    @Provides
    fun provideStatisticDao(
        database: FlashScoreDatabase
    ) = database.statisticDao()


    @Singleton
    @Provides
    fun provideSubstitutionDao(
        database: FlashScoreDatabase
    ) = database.substitutionDao()


    @Singleton
    @Provides
    fun provideEventDao(
        database: FlashScoreDatabase
    ) = database.eventDao()


    @Singleton
    @Provides
    fun provideLeagueRepository(
        leagueDao: LeagueDao,
        api: ApiFootballService
    ) =
        DefaultLeagueRepository(
            leagueDao,
            api
        ) as LeagueRepository

    @Singleton
    @Provides
    fun provideTeamRepository(
        coachDao: CoachDao,
        playerDao: PlayerDao,
        teamDao: TeamDao,
        api: ApiFootballService
    ) =
        DefaultTeamRepository(
            coachDao,
            playerDao,
            teamDao,
            api
        ) as TeamRepository

    @Singleton
    @Provides
    fun provideStandingRepository(
        standingDao: StandingDao,
        api: ApiFootballService
    ) =
        DefaultStandingRepository(
            standingDao,
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
        api: ApiFootballService
    ) =
        DefaultEventRepository(
            cardDao,
            goalscorerDao,
            lineupDao,
            statisticDao,
            substitutionDao,
            eventsDao,
            api
        ) as EventRepository

    @Singleton
    @Provides
    fun provideCountryRepository(
        countryDao: CountryDao,
        api: ApiFootballService
    ) =
        DefaultCountryRepository(
            countryDao,
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


    //@Singleton
    @Provides
    fun provideConnectivityManager(
        application: Application
    ) =
        DefaultConnectivityManager(
            application
        ) as ConnectivityManager


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
    fun provideGlideInstance(
        @ApplicationContext context: Context
    ) = Glide.with(context).setDefaultRequestOptions(
        RequestOptions()
            .placeholder(R.drawable.ic_launcher_background)
            .error(R.drawable.ic_launcher_background)
    )
}