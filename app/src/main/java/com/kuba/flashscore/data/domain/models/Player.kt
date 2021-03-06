package com.kuba.flashscore.data.domain.models

import android.os.Parcelable
import androidx.room.*
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    val teamId: String?,
    val playerAge: String,
    val playerCountry: String,
    val playerGoals: String,
    val playerKey: Long,
    val playerMatchPlayed: String,
    val playerName: String,
    val playerNumber: String,
    val playerRedCards: String,
    val playerType: String,
    val playerYellowCards: String
) : Parcelable