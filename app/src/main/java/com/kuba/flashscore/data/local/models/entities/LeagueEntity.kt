package com.kuba.flashscore.data.local.models.entities

import androidx.room.*
import com.kuba.flashscore.data.domain.models.League
import com.kuba.flashscore.other.DateUtils

@Entity(tableName = "leagues_table")
data class LeagueEntity(
    @ColumnInfo(name = "league_country_id")
    val countryId: String,
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "league_id")
    val leagueId: String,
    @ColumnInfo(name = "league_logo")
    val leagueLogo: String,
    @ColumnInfo(name = "league_name")
    val leagueName: String,
    @ColumnInfo(name = "league_season")
    val leagueSeason: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = DateUtils.dateToLong(
        DateUtils.createTimestamp()
    )
) {

    fun asDomainModel() : League {
        return League(
            countryId,
            leagueId,
            leagueLogo,
            leagueName,
            leagueSeason
        )
    }
}