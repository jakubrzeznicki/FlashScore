package com.kuba.flashscore.network.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CoacheDto(
    @SerializedName("coach_age")
    val coachAge: String,
    @SerializedName("coach_country")
    val coachCountry: String,
    @SerializedName("coach_name")
    val coachName: String
) : Parcelable