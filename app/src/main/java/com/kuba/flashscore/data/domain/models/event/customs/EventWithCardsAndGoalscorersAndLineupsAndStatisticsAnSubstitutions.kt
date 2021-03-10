package com.kuba.flashscore.data.domain.models.event.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.event.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutions(
    val event: Event,
    val cards: List<Card>,
    val goalscorers: List<Goalscorer>,
    val lineups: List<Lineup>,
    val statistics: List<Statistic>,
    val substitutions: List<Substitution>
) : Parcelable