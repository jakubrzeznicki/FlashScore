package com.kuba.flashscore.network.models.events


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CardDto(
    @SerializedName("away_fault")
    val awayFault: String,
    val card: String,
    @SerializedName("home_fault")
    val homeFault: String,
    val info: String,
    val time: String
) : Parcelable