package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Player(
    val playerKey: Long,
    val playerAge: String,
    val playerCountry: String,
    val playerGoals: String,
    val playerMatchPlayed: String,
    val playerName: String,
    val playerNumber: String,
    val playerRedCards: String,
    val playerType: String,
    val playerYellowCards: String
) : Parcelable