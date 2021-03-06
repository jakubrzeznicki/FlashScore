package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "substitution_table",
    primaryKeys = ["substitutions_match_id", "substitution"]
)
data class SubstitutionsEntity(
    @ColumnInfo(name = "substitutions_match_id")
    val matchId: String,
    val substitution: String,
    val time: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
) : Parcelable