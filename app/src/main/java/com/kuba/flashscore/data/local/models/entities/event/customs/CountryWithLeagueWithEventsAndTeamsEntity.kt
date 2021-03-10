package com.kuba.flashscore.data.local.models.entities.event.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.domain.models.event.customs.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity

data class CountryWithLeagueWithEventsAndTeamsEntity(
    @Embedded
    val countryEntity: CountryEntity,

    @Relation(
        parentColumn = "country_id",
        entityColumn = "league_country_id",
        entity = LeagueEntity::class
    )
    val leagueWithTeamEntities: List<LeagueWithTeamsEntity> = emptyList(),

    @Relation(
        parentColumn = "country_id",
        entityColumn = "league_country_id",
        entity = LeagueEntity::class
    )
    val leagueWithEventEntities: List<LeagueWithEventsEntity> = emptyList()
) {
    fun asDomainModel(): CountryWithLeagueWithEventsAndTeams {
        return CountryWithLeagueWithEventsAndTeams(
            countryEntity.asDomainModel(),
            leagueWithTeamEntities.map { it.asDomainModel() },
            leagueWithEventEntities.map { it.asDomainModel() }
        )
    }
}