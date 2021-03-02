package com.kuba.flashscore.local.models.event

import androidx.room.*
import com.kuba.flashscore.local.models.entities.*
import com.kuba.flashscore.local.models.entities.event.CardEntity
import com.kuba.flashscore.local.models.entities.event.SubstitutionsEntity

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Query("SELECT * FROM card_table WHERE card_match_id = :matchId ")
    suspend fun getCardsFromSpecificMatch(matchId: String): List<CardEntity>

}