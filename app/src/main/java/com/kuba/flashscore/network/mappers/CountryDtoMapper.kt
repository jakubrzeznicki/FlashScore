package com.kuba.flashscore.network.mappers

import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.util.LocalMapper
import com.kuba.flashscore.network.models.CountryDto


class CountryDtoMapper : LocalMapper<CountryDto, Country> {

    override fun mapToLocalModel(model: CountryDto): Country {
        return Country(
            countryId = model.countryId,
            countryLogo = model.countryLogo,
            countryName = model.countryName
        )
    }

    override fun mapFromLocalModel(localModel: Country): com.kuba.flashscore.network.models.CountryDto {
        return CountryDto(
            countryId = localModel.countryId,
            countryLogo = localModel.countryLogo,
            countryName = localModel.countryName
        )
    }

    fun toLocalList(initial: List<CountryDto>): List<Country> {
        return initial.map { mapToLocalModel(it) }
    }

    fun fromLocalList(initial: List<Country>): List<CountryDto> {
        return initial.map { mapFromLocalModel(it) }
    }



}