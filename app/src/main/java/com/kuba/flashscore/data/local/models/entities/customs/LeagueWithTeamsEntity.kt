package com.kuba.flashscore.data.local.models.entities.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity

data class LeagueWithTeamsEntity(

    @Embedded
    val league: LeagueEntity,

    @Relation(parentColumn = "league_id", entityColumn = "league_id", entity = TeamEntity::class)
    val teams: List<TeamEntity> = emptyList()
) {
    fun asDomainModel(): LeagueWithTeams {
        return LeagueWithTeams(
            league.asDomainModel(),
            teams.map { it.asDomainModel() }
        )
    }
}