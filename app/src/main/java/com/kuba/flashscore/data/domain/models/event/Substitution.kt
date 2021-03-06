package com.kuba.flashscore.data.domain.models.event

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Substitution(
    val matchId: String,
    val substitution: String,
    val time: String,
    val whichTeam: Boolean
) : Parcelable