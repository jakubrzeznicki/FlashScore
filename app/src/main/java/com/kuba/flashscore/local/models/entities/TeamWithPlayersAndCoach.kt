package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Relation
import androidx.room.Transaction
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamWithPlayersAndCoach(
    @Embedded
    val team: TeamEntity,

    @Relation(parentColumn = "team_id", entityColumn = "player_team_id")
    val players: List<PlayerEntity> = emptyList(),

    @Relation(parentColumn = "team_id", entityColumn = "coach_team_id")
    val coaches: List<CoachEntity> = emptyList()
) : Parcelable