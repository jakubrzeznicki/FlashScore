package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.local.models.entities.TeamEntity
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "substitution_table")
data class SubstitutionsEntity(
    @ColumnInfo(name = "substitutions_match_id")
    val matchId: String,
    val substitution: String,
    val time: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "substitution_id")
    var substitutionId: Int = 0
}