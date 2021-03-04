package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.CountryWithLeagueWithEventsAndTeams
import com.kuba.flashscore.local.models.entities.event.EventEntity
import com.kuba.flashscore.local.models.entities.event.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Query("SELECT * FROM event_table WHERE league_id = :leagueId AND match_date = :date")
    suspend fun getEventsFromSpecificLeague(
        leagueId: String,
        date: String
    ): List<EventEntity>

    @Transaction
    @Query("SELECT * FROM countries_table c  JOIN leagues_table l ON c.country_id = l.league_country_id  JOIN event_table e ON l.league_id = e.league_id WHERE e.league_id = :leagueId AND e.match_date = :date")
    suspend fun getCountryWithLeagueWithTeamsAndEvents(
        leagueId: String,
        date: String
    ): CountryWithLeagueWithEventsAndTeams

    @Transaction
    @Query("SELECT * FROM event_table c WHERE match_id = :eventId")
    suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions

}