package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lineup(
    val matchId: String,
    val lineupNumber: String,
    val lineupPosition: String,
    val whichTeam: Boolean,
    val starting: Boolean,
    val missing: Boolean,
    val playerKey: String
) : Parcelable

