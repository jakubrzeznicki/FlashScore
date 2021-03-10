package com.kuba.flashscore.data.local.daos.event

import androidx.lifecycle.LiveData
import androidx.room.*
import com.kuba.flashscore.data.local.models.entities.event.CardEntity

@Dao
interface CardDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCards(cards: List<CardEntity>)

    @Query("SELECT * FROM card_table WHERE card_match_id = :matchId ")
    suspend fun getCardsFromSpecificMatch(matchId: String): List<CardEntity>
}