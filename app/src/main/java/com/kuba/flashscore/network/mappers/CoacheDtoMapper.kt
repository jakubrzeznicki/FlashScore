package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.data.local.entities.Coache
import com.kuba.flashscore.network.models.CoacheDto
import com.kuba.flashscore.data.local.util.LocalMapper


class CoacheDtoMapper : LocalMapper<CoacheDto, Coache> {


    override fun mapToLocalModel(model: CoacheDto): Coache {
        return Coache(
            coachAge = model.coachAge,
            coachCountry = model.coachCountry,
            coachName = model.coachName
        )
    }

    override fun mapFromLocalModel(localModel: Coache): CoacheDto {
        return CoacheDto(
            coachAge = localModel.coachAge,
            coachCountry = localModel.coachCountry,
            coachName = localModel.coachName
        )
    }


    fun toLocalList(initial: List<CoacheDto>): List<Coache> {
        return initial.map { mapToLocalModel(it) }
    }

    fun fromLocalList(initial: List<Coache>): List<CoacheDto> {
        return initial.map { mapFromLocalModel(it) }
    }


}