package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM event_table WHERE league_id = :leagueId AND match_date = :date")
    suspend fun getEventsFromSpecificLeague(
        leagueId: String,
        date: String
    ): List<EventEntity>

    @Query("SELECT * FROM countries_table c INNER JOIN leagues_table l ON c.country_id = l.league_country_id INNER JOIN event_table e ON l.league_id = e.league_id WHERE l.league_id = :leagueId AND e.match_date = :date")
    suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeams
}