package com.kuba.flashscore.data.network.models


import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.local.models.entities.CountryEntity

data class CountryDto(
    @SerializedName("country_id")
    val countryId: String,
    @SerializedName("country_logo")
    val countryLogo: String,
    @SerializedName("country_name")
    val countryName: String
) {
    fun asLocalModel(): CountryEntity {
        return CountryEntity(
            countryId,
            countryLogo,
            countryName
        )
    }
}