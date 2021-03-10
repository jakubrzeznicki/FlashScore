package com.kuba.flashscore.data.local.models.entities.customs

import androidx.room.*
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity

data class CountryWithLeagueAndTeamsEntity(

    @Embedded
    val countryEntity: CountryEntity,

    @Relation(
        parentColumn = "country_id",
        entityColumn = "league_country_id",
        entity = LeagueEntity::class
    )
    val leagueWithTeamEntities: List<LeagueWithTeamsEntity> = emptyList()
) {
    fun asDomainModel(): CountryWithLeagueAndTeams {
        return CountryWithLeagueAndTeams(
            countryEntity.asDomainModel(),
            leagueWithTeamEntities.map { it.asDomainModel() }
        )
    }
}