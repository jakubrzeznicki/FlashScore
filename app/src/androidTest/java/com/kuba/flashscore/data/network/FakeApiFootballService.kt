package com.kuba.flashscore.data.network

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.kuba.flashscore.data.network.responses.*
import com.kuba.flashscore.other.Constants
import com.kuba.flashscore.util.JsonUtilAndroid
import kotlinx.coroutines.delay
import retrofit2.Response
import javax.inject.Inject

class FakeApiFootballService
@Inject
constructor(
    private val jsonUtilAndroid: JsonUtilAndroid
) : ApiFootballService {

    var countryyJsonFileName: String = Constants.COUNTRY_DATA_FILENAME
    var networkDelay: Long = 0L

    override suspend fun getCountries(APIkey: String): Response<CountryResponse> {
        val rawJson = jsonUtilAndroid.readJSONFromAsset(countryyJsonFileName)
        val countries = Gson().fromJson<CountryResponse>(
            rawJson,
            object : TypeToken<CountryResponse>() {}.type
        )
        delay(networkDelay)
        return Response.success(countries)
    }

    override suspend fun getLeagues(country_id: String, APIkey: String): Response<LeagueResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeams(league_id: String, APIkey: String): Response<TeamResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getTeam(team_id: String, APIkey: String): Response<TeamResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getPlayer(player_name: String, APIkey: String): Response<PlayerResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStandings(
        league_id: String,
        APIkey: String
    ): Response<StandingResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getEvents(
        from: String,
        to: String,
        league_id: String,
        APIkey: String
    ): Response<EventResponse> {
        TODO("Not yet implemented")
    }

}

