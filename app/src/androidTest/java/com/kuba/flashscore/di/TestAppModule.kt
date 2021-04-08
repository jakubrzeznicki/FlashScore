package com.kuba.flashscore.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.kuba.flashscore.data.local.database.FlashScoreDatabase
import com.kuba.flashscore.data.network.FakeApiFootballService
import com.kuba.flashscore.repositories.FakeCountryRepositoryAndroidTest
import com.kuba.flashscore.util.JsonUtilAndroid
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.OkHttpClient
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

//    @Provides
//    fun giveRetrofitAPIService(): ApiFootballService =
//        Retrofit.Builder()
//            .baseUrl("http://localhost:8080/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//            .build()
//            .create(ApiFootballService::class.java)


    @Provides
    fun provideUrl(): String = "http://localhost:8080/"


    @Provides
    fun provideOkHttpClient(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides
    @Singleton
    fun provideJsonUtil(application: Application): JsonUtilAndroid {
        return JsonUtilAndroid(application)
    }

    @Provides
    fun provideFakeApiFootballService(jsonUtilAndroid: JsonUtilAndroid): FakeApiFootballService {
        return FakeApiFootballService(jsonUtilAndroid)
    }

    @Provides
    fun provideFakCountryRepository(): FakeCountryRepositoryAndroidTest {
        return FakeCountryRepositoryAndroidTest()
    }
}