package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.local.models.entities.CoachEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.models.CoacheDto
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.other.DomainMapper

class CoachDtoMapper : DomainMapper<CoacheDto, CoachEntity> {
    override fun mapToLocalModel(model: CoacheDto, foreignKey: String?): CoachEntity {
        return CoachEntity(
            teamId = foreignKey,
            coachAge = model.coachAge,
            coachName = model.coachName,
            coachCountry = model.coachCountry,
        )
    }

    fun toLocalList(initial: List<CoacheDto>, foreignKey: String?): List<CoachEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }

}