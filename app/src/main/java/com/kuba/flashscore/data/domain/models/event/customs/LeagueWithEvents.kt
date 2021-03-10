package com.kuba.flashscore.data.domain.models.event.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.League
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.local.models.entities.LeagueEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueWithEvents(
    val league: League,
    val eventsWithEventInformation: List<EventWithEventInformation>
) : Parcelable