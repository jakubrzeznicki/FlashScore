package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.data.local.entities.Team
import com.kuba.flashscore.network.models.TeamDto


import com.kuba.flashscore.data.local.util.LocalMapper


class TeamDtoMapper : LocalMapper<TeamDto, Team> {


    override fun mapToLocalModel(model: TeamDto): Team {
        val localPlayer = PlayerDtoMapper().toLocalList(model.players)
        val localCoache = CoacheDtoMapper().toLocalList(model.coaches)
        return Team(
            teamBadge = model.teamBadge,
            teamKey = model.teamKey,
            teamName = model.teamName,
            coaches = localCoache,
            players = localPlayer
        )
    }

    override fun mapFromLocalModel(localModel: Team): TeamDto {
        val player = PlayerDtoMapper().fromLocalList(localModel.players)
        val coache = CoacheDtoMapper().fromLocalList(localModel.coaches)
        return TeamDto(
            teamBadge = localModel.teamBadge,
            teamKey = localModel.teamKey,
            teamName = localModel.teamName,
            coaches = coache,
            players = player
        )
    }


    fun toLocalList(initial: List<TeamDto>): List<Team> {
        return initial.map { mapToLocalModel(it) }
    }


    fun fromLocalList(initial: List<Team>): List<TeamDto> {
        return initial.map { mapFromLocalModel(it) }
    }


}