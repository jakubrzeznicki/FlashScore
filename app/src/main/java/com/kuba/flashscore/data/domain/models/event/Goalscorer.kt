package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Goalscorer(
    val matchId: String,
    val assistId: String,
    val scorerId: String,
    val whichTeam: Boolean,
    val info: String,
    val score: String,
    val time: String
) : Parcelable