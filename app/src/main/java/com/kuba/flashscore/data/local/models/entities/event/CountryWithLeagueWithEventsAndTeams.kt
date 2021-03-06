package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.CountryEntity
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import com.kuba.flashscore.data.local.models.entities.LeagueWithTeams
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