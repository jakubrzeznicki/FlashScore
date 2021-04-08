package com.kuba.flashscore.data.network

import com.google.common.truth.Truth
import com.google.common.truth.Truth.assertThat
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.network.data.MockResponseFileReader
import com.kuba.flashscore.data.network.models.*
import com.kuba.flashscore.data.network.models.events.EventDto
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
//@ExperimentalSerializationApi
class ApiFootballServiceTest {

    lateinit var mockWebServer: MockWebServer

    private val client = OkHttpClient.Builder()
        .connectTimeout(1, TimeUnit.SECONDS)
        .readTimeout(1, TimeUnit.SECONDS)
        .writeTimeout(1, TimeUnit.SECONDS)
        .build()

    private lateinit var api: ApiFootballService


    @Before
    fun setUp() {
        mockWebServer = MockWebServer()
        mockWebServer.start()
        api = Retrofit.Builder()
            .baseUrl(mockWebServer.url("/"))
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiFootballService::class.java)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
    }

    @Test
    fun fetchCountriesDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_country.json").content)
        })
        runBlocking {
            val actual = api.getCountries()

            val expected = CountryDto(
                countryId = "41",
                countryName = "England",
                countryLogo = "https://apiv2.apifootball.com/badges/logo_country/41_england.png"
            )

            assertThat(actual.body()?.toList()).contains(expected)
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchCountriesDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getCountries()

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.countryId).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }

    @Test
    fun fetchLeaguesDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_league.json").content)
        })
        runBlocking {
            val actual = api.getLeagues("41")

            val expected = LeagueDto(
                countryId = "41",
                countryLogo = "https://apiv2.apifootball.com/badges/logo_country/41_england.png",
                countryName = "England",
                leagueId = "149",
                leagueLogo = "https://apiv2.apifootball.com/badges/logo_leagues/149_championship.png",
                leagueSeason = "2020/2021",
                leagueName = "Championship"
            )

            assertThat(actual.body()?.toList()).contains(expected)
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchLeaguesDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getLeagues("41")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.leagueId).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }

    @Test
    fun fetchTeamsDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_team.json").content)
        })
        runBlocking {
            val actual = api.getTeams("148")

            val expected = TeamDto(
                teamKey = "2611",
                teamName = "Leicester",
                teamBadge = "https://apiv2.apifootball.com/badges/2611_leicester.png",
                coaches = listOf(
                    CoacheDto(
                        coachName = "Rodgers Brendan",
                        coachCountry = "Northern Ireland",
                        coachAge = "48"
                    )
                ),
                players = emptyList()
            )

            assertThat(actual.body()?.toList()?.get(0)?.teamKey).isEqualTo(expected.teamKey)
            assertThat(actual.body()?.toList()?.get(0)?.teamName).isEqualTo(expected.teamName)
            assertThat(actual.body()?.toList()?.get(0)?.teamBadge).isEqualTo(expected.teamBadge)
            assertThat(actual.body()?.toList()?.get(0)?.coaches).isEqualTo(expected.coaches)
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchTeamsDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getTeams("148")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.teamKey).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }


    @Test
    fun fetchTeamDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_team.json").content)
        })
        runBlocking {
            val actual = api.getTeam("2611")

            val expected = TeamDto(
                teamKey = "2611",
                teamName = "Leicester",
                teamBadge = "https://apiv2.apifootball.com/badges/2611_leicester.png",
                coaches = listOf(
                    CoacheDto(
                        coachName = "Rodgers Brendan",
                        coachCountry = "Northern Ireland",
                        coachAge = "48"
                    )
                ),
                players = emptyList()
            )

            assertThat(actual.body()?.toList()?.get(0)?.teamKey).isEqualTo(expected.teamKey)
            assertThat(actual.body()?.toList()?.get(0)?.teamName).isEqualTo(expected.teamName)
            assertThat(actual.body()?.toList()?.get(0)?.teamBadge).isEqualTo(expected.teamBadge)
            assertThat(actual.body()?.toList()?.get(0)?.coaches).isEqualTo(expected.coaches)
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchTeamDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getTeam("2611")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.teamKey).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }

    @Test
    fun fetchPlayerDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_player.json").content)
        })
        runBlocking {
            val actual = api.getPlayer("Ronaldo Cristiano")

            val expected = PlayerInfoDto(
                playerAge = "36",
                playerCountry = "Portugal",
                playerYellowCards = "1",
                playerRedCards = "0",
                playerMatchPlayed = "18",
                playerGoals = "16",
                playerType = "Forwards",
                playerNumber = "7",
                playerName = "Ronaldo Cristiano",
                playerKey = 3183500916,
                teamName = "Juventus",
                teamKey = "4187"
            )

            assertThat(actual.body()?.toList()?.get(1)).isEqualTo(expected)
        }
    }

    @Test
    fun fetchPlayerDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getPlayer("Ronaldo Cristiano")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.teamKey).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }


    @Test
    fun fetchStandingsDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_standing.json").content)
        })
        runBlocking {
            val actual = api.getStandings("148")

            val expected = StandingDto(
                awayLeagueD = "3",
                awayLeagueGA = "8",
                awayLeagueGF = "28",
                awayLeagueL = "1",
                awayLeaguePTS = "33",
                awayLeaguePayed = "14",
                awayLeaguePosition = "2",
                awayLeagueW = "10",
                awayPromotion = "",
                countryName = "England",
                homeLeagueD = "2",
                homeLeagueGA = "13",
                homeLeagueGF = "36",
                homeLeagueL = "2",
                homeLeaguePTS = "38",
                homeLeaguePayed = "16",
                homeLeaguePosition = "1",
                homeLeagueW = "12",
                homePromotion = "",
                leagueId = "148",
                leagueName = "Premier League",
                leagueRound = "",
                overallLeagueD = "5",
                overallLeagueGA = "21",
                overallLeagueGF = "64",
                overallLeagueL = "3",
                overallLeaguePTS = "71",
                overallLeaguePayed = "30",
                overallLeaguePosition = "1",
                overallLeagueW = "22",
                overallPromotion = "Promotion - Champions League (Group Stage)",
                teamBadge = "https://apiv2.apifootball.com/badges/2626_manchester-city.png",
                teamId = "2626",
                teamName = "Manchester City"
            )

            assertThat(actual.body()?.toList()).contains(expected)
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchStandingDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getStandings("148")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.leagueId).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }

    @Test
    fun fetchEventsDataFromApi_returnSuccess() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(MockResponseFileReader("success_event.json").content)
        })
        runBlocking {
            val actual = api.getEvents("2021-03-02", "2021-03-02", "148")
            val expectedMatchId = "411953"
            val expectedMatchDate = "2021-03-02"
            val expectedMatchStatus = "Finished"
            val expectedMatchHomeId = "2637"
            val expectedMatchAwayId = "2642"

            assertThat(actual.body()?.toList()?.get(0)?.matchId).isEqualTo(expectedMatchId)
            assertThat(actual.body()?.toList()?.get(0)?.matchDate).isEqualTo(expectedMatchDate)
            assertThat(actual.body()?.toList()?.get(0)?.matchStatus).isEqualTo(expectedMatchStatus)
            assertThat(actual.body()?.toList()?.get(0)?.matchHometeamId).isEqualTo(
                expectedMatchHomeId
            )
            assertThat(actual.body()?.toList()?.get(0)?.matchAwayteamId).isEqualTo(
                expectedMatchAwayId
            )
            assertThat(actual.isSuccessful).isTrue()
            assertThat(actual.code()).isEqualTo(200)
        }
    }

    @Test
    fun fetchEventsDataFromApi_returnError() {
        mockWebServer.enqueue(MockResponse().apply {
            setResponseCode(404)
            setBody(MockResponseFileReader("error.json").content)
        })
        runBlocking {
            val actual = api.getEvents("2021-03-02", "2021-03-02", "148")

            //assertThat(mockWebServer.takeRequest().body.readUtf8()).contains("404")
            assertThat(actual.body()?.toList()?.get(0)?.matchId).isNull()
            //assertThat(actual.isSuccessful).isFalse()
            assertThat(actual.code()).isEqualTo(404)
        }
    }


}