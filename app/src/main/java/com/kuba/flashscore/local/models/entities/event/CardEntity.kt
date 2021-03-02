package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "card_table")
data class CardEntity(
    @ColumnInfo(name = "card_match_id")
    val matchId: String,
    val fault: String,
    val card: String,
    @ColumnInfo(name = "which_team")
    val whichTeam: Boolean,
    val info: String,
    val time: String,

    ) : Parcelable {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    var cardId: Int = 0
}