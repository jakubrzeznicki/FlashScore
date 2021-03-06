package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_table", primaryKeys = ["time", "fault", "card_match_id"])
data class CardEntity(
    @ColumnInfo(name = "card_match_id")
    val matchId: String,
    val fault: String,
    val card: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val info: String,
    val time: String
) : Parcelable