package com.kuba.flashscore.data.local.models.entities

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.Player
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "player_table")
data class PlayerEntity(
    @ColumnInfo(name = "player_team_id")
    val teamId: String,
    @ColumnInfo(name = "player_age")
    val playerAge: String,
    @ColumnInfo(name ="player_country")
    val playerCountry: String,
    @ColumnInfo(name ="player_goals")
    val playerGoals: String,
    @ColumnInfo(name ="player_id")
    @PrimaryKey(autoGenerate = false)
    val playerKey: Long,
    @ColumnInfo(name ="player_match_played")
    val playerMatchPlayed: String,
    @ColumnInfo(name ="player_name")
    val playerName: String,
    @ColumnInfo(name ="player_number")
    val playerNumber: String,
    @ColumnInfo(name ="player_red_cards")
    val playerRedCards: String,
    @ColumnInfo(name ="player_type")
    val playerType: String,
    @ColumnInfo(name ="player_yellow_cards")
    val playerYellowCards: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {
    fun asDomainModel() : Player {
        return Player(
            teamId,
            playerAge,
            playerCountry,
            playerGoals,
            playerKey,
            playerMatchPlayed,
            playerName,
            playerNumber,
            playerRedCards,
            playerType,
            playerYellowCards
        )
    }
}