package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.data.local.entities.Standing
import com.kuba.flashscore.data.local.entities.Team
import com.kuba.flashscore.network.models.TeamDto


import com.kuba.flashscore.data.local.util.LocalMapper
import com.kuba.flashscore.network.models.StandingDto


class StandingDtoMapper : LocalMapper<StandingDto, Standing> {


    override fun mapToLocalModel(model: StandingDto): Standing {
        return Standing(
            awayLeagueD = model.awayLeagueD,
            awayLeagueGA = model.awayLeagueGA,
            awayLeagueGF = model.awayLeagueGF,
            awayLeagueL = model.awayLeagueL,
            awayLeaguePTS = model.awayLeaguePTS,
            awayLeaguePayed = model.awayLeaguePayed,
            awayLeaguePosition = model.awayLeaguePosition,
            awayLeagueW = model.awayLeagueW,
            awayPromotion = model.awayPromotion,
            countryName = model.countryName,
            homeLeagueD = model.homeLeagueD,
            homeLeagueGA = model.homeLeagueGA,
            homeLeagueGF = model.homeLeagueGF,
            homeLeagueL = model.homeLeagueL,
            homeLeaguePTS = model.homeLeaguePTS,
            homeLeaguePayed = model.homeLeaguePayed,
            homeLeaguePosition = model.homeLeaguePosition,
            homeLeagueW = model.homeLeagueW,
            homePromotion = model.homePromotion,
            leagueId = model.leagueId,
            leagueName = model.leagueName,
            leagueRound = model.leagueRound,
            overallLeagueD = model.overallLeagueD,
            overallLeagueGA = model.overallLeagueGA,
            overallLeagueGF = model.overallLeagueGF,
            overallLeagueL = model.overallLeagueL,
            overallLeaguePTS = model.overallLeaguePTS,
            overallLeaguePayed = model.overallLeaguePayed,
            overallLeaguePosition = model.overallLeaguePosition,
            overallLeagueW = model.overallLeagueW,
            overallPromotion = model.overallPromotion,
            teamBadge = model.teamBadge,
            teamId = model.teamId,
            teamName = model.teamName
        )
    }

    override fun mapFromLocalModel(localModel: Standing): StandingDto {

        return StandingDto(
            awayLeagueD = localModel.awayLeagueD,
            awayLeagueGA = localModel.awayLeagueGA,
            awayLeagueGF = localModel.awayLeagueGF,
            awayLeagueL = localModel.awayLeagueL,
            awayLeaguePTS = localModel.awayLeaguePTS,
            awayLeaguePayed = localModel.awayLeaguePayed,
            awayLeaguePosition = localModel.awayLeaguePosition,
            awayLeagueW = localModel.awayLeagueW,
            awayPromotion = localModel.awayPromotion,
            countryName = localModel.countryName,
            homeLeagueD = localModel.homeLeagueD,
            homeLeagueGA = localModel.homeLeagueGA,
            homeLeagueGF = localModel.homeLeagueGF,
            homeLeagueL = localModel.homeLeagueL,
            homeLeaguePTS = localModel.homeLeaguePTS,
            homeLeaguePayed = localModel.homeLeaguePayed,
            homeLeaguePosition = localModel.homeLeaguePosition,
            homeLeagueW = localModel.homeLeagueW,
            homePromotion = localModel.homePromotion,
            leagueId = localModel.leagueId,
            leagueName = localModel.leagueName,
            leagueRound = localModel.leagueRound,
            overallLeagueD = localModel.overallLeagueD,
            overallLeagueGA = localModel.overallLeagueGA,
            overallLeagueGF = localModel.overallLeagueGF,
            overallLeagueL = localModel.overallLeagueL,
            overallLeaguePTS = localModel.overallLeaguePTS,
            overallLeaguePayed = localModel.overallLeaguePayed,
            overallLeaguePosition = localModel.overallLeaguePosition,
            overallLeagueW = localModel.overallLeagueW,
            overallPromotion = localModel.overallPromotion,
            teamBadge = localModel.teamBadge,
            teamId = localModel.teamId,
            teamName = localModel.teamName
        )
    }


    fun toLocalList(initial: List<StandingDto>): List<Standing> {
        return initial.map { mapToLocalModel(it) }
    }

    fun fromLocalList(initial: List<Standing>): List<StandingDto> {
        return initial.map { mapFromLocalModel(it) }
    }


}