package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.other.DomainMapper

class LeagueDtoMapper : DomainMapper<LeagueDto, LeagueEntity> {
    override fun mapToLocalModel(model: LeagueDto, foreignKey: String?): LeagueEntity {
        return LeagueEntity(
            countryId = model.countryId,
            leagueId = model.leagueId,
            leagueName = model.leagueName,
            leagueSeason = model.leagueSeason,
            leagueLogo = model.leagueLogo
        )
    }

    fun toLocalList(initial: List<LeagueDto>, foreignKey: String?): List<LeagueEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }
}