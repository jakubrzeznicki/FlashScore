package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueWithEvents(

    @Embedded
    val league: LeagueEntity,

    @Relation(
        parentColumn = "league_id",
        entityColumn = "league_id",
        entity = EventEntity::class
    )
    val events: List<EventEntity?> = emptyList()
) : Parcelable