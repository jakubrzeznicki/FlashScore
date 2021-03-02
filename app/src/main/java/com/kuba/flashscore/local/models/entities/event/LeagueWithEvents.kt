package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.local.models.entities.LeagueEntity
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
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