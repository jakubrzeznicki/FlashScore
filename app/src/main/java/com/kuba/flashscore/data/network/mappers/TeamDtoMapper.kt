package com.kuba.flashscore.data.network.mappers

import com.kuba.flashscore.data.local.models.entities.TeamEntity
import com.kuba.flashscore.data.network.models.TeamDto
import com.kuba.flashscore.other.DomainMapper

class TeamDtoMapper : DomainMapper<TeamDto, TeamEntity> {
    override fun mapToLocalModel(model: TeamDto, foreignKey: String?): TeamEntity {
        return TeamEntity(
            leagueId = foreignKey,
            teamBadge = model.teamBadge,
            teamKey = model.teamKey,
            teamName = model.teamName
        )
    }

    fun toLocalList(initial: List<TeamDto>, foreignKey: String?): List<TeamEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }

}