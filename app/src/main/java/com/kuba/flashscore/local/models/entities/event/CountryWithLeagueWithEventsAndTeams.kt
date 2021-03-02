package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.local.models.entities.CountryEntity
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.local.models.entities.LeagueWithTeams
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryWithLeagueWithEventsAndTeams(
    @Embedded
    val countryEntity: CountryEntity,

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id", entity = LeagueEntity::class)
    val leagueWithTeams: List<LeagueWithTeams> = emptyList(),

    @Relation(parentColumn = "country_id", entityColumn = "league_country_id", entity = LeagueEntity::class)
    val leagueWithEvents: List<LeagueWithEvents> = emptyList()

) : Parcelable