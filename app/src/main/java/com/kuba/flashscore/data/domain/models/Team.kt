package com.kuba.flashscore.data.domain.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val leagueId: String?,
    val teamBadge: String,
    val teamKey: String,
    val teamName: String
) : Parcelable