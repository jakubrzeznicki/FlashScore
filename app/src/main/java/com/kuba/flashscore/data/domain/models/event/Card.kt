package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Card(
    val matchId: String,
    val fault: String,
    val card: String,
    val whichTeam: Boolean,
    val info: String,
    val time: String
) : Parcelable