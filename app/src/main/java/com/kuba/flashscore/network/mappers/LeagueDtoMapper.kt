package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.local.models.entities.CountryAndLeagues
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.network.models.LeagueDto
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.DomainMapper

class LeagueDtoMapper : DomainMapper<LeagueDto, LeagueEntity> {
    override fun mapToDomainModel(model: LeagueDto): LeagueEntity {
        return LeagueEntity(
            countryId = model.countryId,
            leagueId = model.leagueId,
            leagueName = model.leagueName,
            leagueSeason = model.leagueSeason,
            leagueLogo = model.leagueLogo
        )
    }

    override fun mapFromDomainModel(domainModel: LeagueEntity): LeagueDto {
        return LeagueDto(
            countryId = domainModel.countryId,
            leagueId = domainModel.leagueId,
            leagueName = domainModel.leagueName,
            leagueSeason = domainModel.leagueSeason,
            leagueLogo = domainModel.leagueLogo,
            countryName = "",
            countryLogo = ""
        )
    }

    fun toDomainList(initial: List<LeagueDto>): List<LeagueEntity> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<LeagueEntity>): List<LeagueDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}