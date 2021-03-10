package com.kuba.flashscore.repositories.standing

import androidx.lifecycle.LiveData
import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.other.Resource

interface StandingRepository {
    suspend fun insertStandings(standings: List<StandingEntity>)
    suspend fun refreshStandingsFromSpecificLeague(leagueId: String): Resource<List<Standing>>
    suspend fun getStandingsFromSpecificLeagueFromDb(leagueId: String, standingType: StandingType): List<StandingEntity>
    suspend fun getAllStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity>
}
