package com.kuba.flashscore.data.local.models.entities.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Substitution
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

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
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel() : Substitution {
        return Substitution(
            matchId,
            substitution,
            time,
            whichTeam
        )
    }
}