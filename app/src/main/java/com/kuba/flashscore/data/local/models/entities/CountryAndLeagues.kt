package com.kuba.flashscore.data.local.models.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryAndLeagues(
    @Embedded
    val country: CountryEntity,

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id")
    val leagues: List<LeagueEntity> = emptyList()
) :Parcelable