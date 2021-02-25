package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.local.models.entities.CoachEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.network.models.CoacheDto
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.network.models.TeamDto
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