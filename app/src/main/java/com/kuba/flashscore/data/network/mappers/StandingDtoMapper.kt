package com.kuba.flashscore.data.network.mappers

import com.kuba.flashscore.data.local.models.entities.StandingEntity
import com.kuba.flashscore.data.network.models.StandingDto
import com.kuba.flashscore.other.DomainMapper

class StandingDtoMapper : DomainMapper<StandingDto, StandingEntity> {
    override fun mapToLocalModel(model: StandingDto, foreignKey: String?): StandingEntity {
        return StandingEntity(
            awayLeagueD = model.awayLeagueD,
            awayLeagueGA = model.awayLeagueGA,
            awayLeagueGF = model.awayLeagueGF,
            awayLeagueL = model.awayLeagueL,
            awayLeaguePTS = model.awayLeaguePTS,
            awayLeaguePayed = model.awayLeaguePayed,
            awayLeaguePosition = model.awayLeaguePosition,
            awayLeagueW = model.awayLeagueW,
            awayPromotion = model.awayPromotion,
            homeLeagueD = model.homeLeagueD,
            homeLeagueGA = model.homeLeagueGA,
            homeLeagueGF = model.homeLeagueGF,
            homeLeagueL = model.homeLeagueL,
            homeLeaguePTS = model.homeLeaguePTS,
            homeLeaguePayed = model.homeLeaguePayed,
            homeLeaguePosition = model.homeLeaguePosition,
            homeLeagueW = model.homeLeagueW,
            homePromotion = model.homePromotion,
            leagueRound = model.leagueRound,
            overallLeagueD = model.overallLeagueD,
            overallLeagueGA = model.overallLeagueGA,
            overallLeagueGF = model.overallLeagueGF,
            overallLeaguePayed = model.overallLeaguePayed,
            overallLeagueL = model.overallLeagueL,
            overallLeaguePosition = model.overallLeaguePosition,
            overallLeaguePTS = model.overallLeaguePTS,
            overallLeagueW = model.overallLeagueW,
            overallPromotion = model.overallPromotion,
            leagueId = model.leagueId,
            teamId = model.teamId
        )
    }

    fun toLocalList(initial: List<StandingDto>, foreignKey: String?): List<StandingEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }

}