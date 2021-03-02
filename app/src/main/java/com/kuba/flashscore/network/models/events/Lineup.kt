package com.kuba.flashscore.network.models.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class Lineup(
    @SerializedName("away")
    val away: Away,
    @SerializedName("home")
    val home: Home
)