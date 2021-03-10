package com.kuba.flashscore.data.domain.models.event.customs

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.data.domain.models.event.Event
import com.kuba.flashscore.data.domain.models.event.EventInformation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventWithEventInformation(
    val event: Event,
    val eventInformation: List<EventInformation>
) : Parcelable