package com.kuba.flashscore.repositories.standing

import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.other.Resource

interface StandingRepository {
    suspend fun insertStandings(standings: List<StandingEntity>)
    suspend fun getStandingsFromSpecificLeagueFromNetwork(leagueId: String): Resource<List<StandingEntity>>
    suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity>
}
