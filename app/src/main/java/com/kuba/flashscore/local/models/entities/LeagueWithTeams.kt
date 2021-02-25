package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueWithTeams(

    @Embedded
    val league: LeagueEntity,

    @Relation(parentColumn = "league_id", entityColumn = "team_league_id", entity = TeamEntity::class)
    val teams: List<TeamEntity> = emptyList()
) : Parcelable