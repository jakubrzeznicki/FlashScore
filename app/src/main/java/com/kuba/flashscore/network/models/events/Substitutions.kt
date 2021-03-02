package com.kuba.flashscore.network.models.events

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


data class Substitutions(
    val away: List<AwayX>,
    val home: List<HomeX>
)