package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Team(
    val teamKey: String,
    val teamName: String,
    val teamBadge: String,
    val coaches: List<Coache>,
    val players: List<Player>
) : Parcelable