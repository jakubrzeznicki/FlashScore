package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.League
import com.kuba.flashscore.data.local.util.LocalMapper
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.network.models.LeagueDto


class LeagueDtoMapper : LocalMapper<LeagueDto, League> {


    override fun mapToLocalModel(model: LeagueDto): League {
        return League(
            leagueId = model.leagueId,
            leagueLogo = model.leagueLogo,
            leagueName = model.leagueName,
            leagueSeason = model.leagueSeason,
            countryId = model.countryId,
            countryName = model.countryName,
            countryLogo = model.countryLogo
        )
    }

    override fun mapFromLocalModel(localModel: League): LeagueDto {
        return LeagueDto(
            leagueId = localModel.leagueId,
            leagueLogo = localModel.leagueLogo,
            leagueName = localModel.leagueName,
            leagueSeason = localModel.leagueSeason,
            countryId = localModel.countryId,
            countryName = localModel.countryName,
            countryLogo = localModel.countryLogo
        )
    }


    fun toLocalList(initial: List<LeagueDto>): List<League> {
        return initial.map { mapToLocalModel(it) }
    }

    fun fromLocalList(initial: List<League>): List<LeagueDto> {
        return initial.map { mapFromLocalModel(it) }
    }


}