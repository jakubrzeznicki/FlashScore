package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryWithLeagueAndTeams(

    @Embedded
    val countryEntity: CountryEntity,

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id", entity = LeagueEntity::class)
    val teams: List<LeagueWithTeams> = emptyList()

) : Parcelable