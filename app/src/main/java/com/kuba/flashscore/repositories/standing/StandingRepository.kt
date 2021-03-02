package com.kuba.flashscore.repositories.standing

import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.*
import com.kuba.flashscore.network.responses.*
import com.kuba.flashscore.other.Resource

interface StandingRepository {
    suspend fun insertStandings(standings: List<StandingEntity>)
    suspend fun getStandingsFromSpecificLeague(leagueId: String): Resource<StandingResponse>
    suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity>
}
