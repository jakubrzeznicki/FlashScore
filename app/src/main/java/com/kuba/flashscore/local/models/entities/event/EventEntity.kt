package com.kuba.flashscore.local.models.entities.event

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.network.models.events.*
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(
    tableName = "event_table"
)
data class EventEntity(
    @ColumnInfo(name = "league_id")
    val leagueId: String,
    @ColumnInfo(name = "match_awayteam_extra_score")
    val matchAwayteamExtraScore: String,
    @ColumnInfo(name = "match_awayteam_ft_score")
    val matchAwayteamFtScore: String,
    @ColumnInfo(name = "match_awayteam_halftime_score")
    val matchAwayteamHalftimeScore: String,
    @ColumnInfo(name = "match_awayteam_id")
    val matchAwayteamId: String,
    @ColumnInfo(name = "match_awayteam_penalty_score")
    val matchAwayteamPenaltyScore: String,
    @ColumnInfo(name = "match_awayteam_score")
    val matchAwayteamScore: String,
    @ColumnInfo(name = "match_awayteam_system")
    val matchAwayteamSystem: String,
    @ColumnInfo(name = "match_date")
    val matchDate: String,
    @ColumnInfo(name = "match_hometeam_extra_score")
    val matchHometeamExtraScore: String,
    @ColumnInfo(name = "match_hometeam_ft_score")
    val matchHometeamFtScore: String,
    @ColumnInfo(name = "match_hometeam_halftime_score")
    val matchHometeamHalftimeScore: String,
    @ColumnInfo(name = "match_hometeam_id")
    val matchHometeamId: String,
    @ColumnInfo(name = "match_hometeam_penalty_score")
    val matchHometeamPenaltyScore: String,
    @ColumnInfo(name = "match_hometeam_score")
    val matchHometeamScore: String,
    @ColumnInfo(name = "match_hometeam_system")
    val matchHometeamSystem: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "match_id")
    val matchId: String,
    @ColumnInfo(name = "match_live")
    val matchLive: String,
    @ColumnInfo(name = "match_referee")
    val matchReferee: String,
    @ColumnInfo(name = "match_round")
    val matchRound: String,
    @ColumnInfo(name = "match_stadium")
    val matchStadium: String,
    @ColumnInfo(name = "match_status")
    val matchStatus: String,
    @ColumnInfo(name = "match_time")
    val matchTime: String,

) : Parcelable