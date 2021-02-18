package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.DomainMapper

class CountryDtoMapper : DomainMapper<CountryDto, CountryEntity> {
    override fun mapToDomainModel(model: CountryDto): CountryEntity {
        return CountryEntity(
            countryId = model.countryId,
            countryLogo = model.countryLogo,
            countryName = model.countryName,
            dateCached = DateUtils.dateToLong(
                DateUtils.createTimestamp()
            )
        )
    }

    override fun mapFromDomainModel(domainModel: CountryEntity): CountryDto {
        return CountryDto(
            countryId = domainModel.countryId,
            countryLogo = domainModel.countryLogo,
            countryName = domainModel.countryName,
        )
    }

    fun toDomainList(initial: List<CountryDto>): List<CountryEntity> {
        return initial.map { mapToDomainModel(it) }
    }

    fun fromDomainList(initial: List<CountryEntity>): List<CountryDto> {
        return initial.map { mapFromDomainModel(it) }
    }
}