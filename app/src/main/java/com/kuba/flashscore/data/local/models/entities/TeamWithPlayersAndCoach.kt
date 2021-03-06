package com.kuba.flashscore.data.local.models.entities

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Relation
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