package com.kuba.flashscore.data.local.entities

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Country(
    @PrimaryKey(autoGenerate = false)
    val countryId: String,
    val countryLogo: String,
    val countryName: String
) : Parcelable