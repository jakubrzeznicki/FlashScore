package com.kuba.flashscore.repositories.standings

import com.kuba.flashscore.data.domain.models.Standing
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.other.Resource
import com.kuba.flashscore.repositories.standing.StandingRepository

class FakeStandingsRepositoryAndroidTest : StandingRepository {

    private val standingItems = mutableListOf<StandingEntity>()

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertStandings(standings: List<StandingEntity>) {
        standingItems.addAll(standings)
    }

    override suspend fun refreshStandingsFromSpecificLeague(leagueId: String): Resource<List<Standing>> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(standingItems.map { it.asDomainModel() })
        }
    }

    override suspend fun getStandingsFromSpecificLeagueFromDb(
        leagueId: String,
        standingType: StandingType
    ): List<StandingEntity> {
        return standingItems.filter { it.standingType == standingType }
    }

    override suspend fun getAllStandingsFromSpecificLeagueFromDb(leagueId: String): List<StandingEntity> {
        return standingItems
    }
}
