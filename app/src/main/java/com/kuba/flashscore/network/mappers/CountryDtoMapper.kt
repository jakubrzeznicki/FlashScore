package com.kuba.flashscore.network.mappers

import androidx.room.ForeignKey
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.network.models.CountryDto
import com.kuba.flashscore.other.DateUtils
import com.kuba.flashscore.other.DomainMapper

class CountryDtoMapper : DomainMapper<CountryDto, CountryEntity> {

    override fun mapToLocalModel(model: CountryDto, foreignKey: String?): CountryEntity {
        return CountryEntity(
            countryId = model.countryId,
            countryLogo = model.countryLogo,
            countryName = model.countryName,
            dateCached = DateUtils.dateToLong(
                DateUtils.createTimestamp()
            )
        )
    }


    fun toLocalList(initial: List<CountryDto>, foreignKey: String?): List<CountryEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }


}