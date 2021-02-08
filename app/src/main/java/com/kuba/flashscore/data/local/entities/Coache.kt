package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Coache(
    val id: Int? = null,
    val coachAge: String,
    val coachCountry: String,
    val coachName: String
) : Parcelable