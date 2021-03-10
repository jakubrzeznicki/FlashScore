package com.kuba.flashscore.data.local.daos.event

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.EventEntity
import com.kuba.flashscore.data.local.models.entities.event.EventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity

@Dao
interface EventDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEvents(events: List<EventEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEventInformation(eventInformation: List<EventInformationEntity>)

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
    ): CountryWithLeagueWithEventsAndTeamsEntity

    @Transaction
    @Query("SELECT * FROM event_table c WHERE match_id = :eventId")
    suspend fun getEventWithCardsAndGoalscorersAndLineupsAndStatisticsAndSubstitutions(
        eventId: String
    ): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity

}