package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueWithTeamsAndStandings(
    @Embedded
    val league: LeagueEntity,

    @Relation(parentColumn = "league_id", entityColumn = "league_id")
    val teams: List<TeamEntity> = emptyList(),

    @Relation(parentColumn = "league_id", entityColumn = "league_id")
    val standings: List<StandingEntity> = emptyList()
) : Parcelable