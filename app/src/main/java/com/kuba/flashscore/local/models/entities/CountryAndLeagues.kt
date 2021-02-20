package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryAndLeagues(
    @Embedded
    val country: CountryEntity,

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id")
    @ColumnInfo(name = "leagues_list")
    val leagues: List<LeagueEntity> = emptyList()
) : Parcelable