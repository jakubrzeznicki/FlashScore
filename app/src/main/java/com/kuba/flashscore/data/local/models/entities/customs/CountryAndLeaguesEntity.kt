package com.kuba.flashscore.data.local.models.entities.customs

import androidx.room.Embedded
import androidx.room.Relation
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity

data class CountryAndLeaguesEntity(
    @Embedded
    val country: CountryEntity,

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id")
    val leagues: List<LeagueEntity> = emptyList()
) {
    fun asDomainModel(): CountryAndLeagues {
        return CountryAndLeagues(
            country.asDomainModel(),
            leagues.map { it.asDomainModel() }
        )
    }
}