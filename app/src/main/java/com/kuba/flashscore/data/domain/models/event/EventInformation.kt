package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import com.kuba.flashscore.other.DateUtils
import kotlinx.android.parcel.Parcelize

@Parcelize
data class EventInformation(
    val matchId: String,
    val extraScore: String,
    val fullTImeScore: String,
    val halftimeScore: String,
    val teamId: String,
    val teamPenaltyScore: String,
    val teamScore: String,
    val teamSystem: String,
    val isHome: Boolean
) : Parcelable