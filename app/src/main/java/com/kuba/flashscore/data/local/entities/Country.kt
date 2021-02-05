package com.kuba.flashscore.data.local.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country(
    @PrimaryKey(autoGenerate = false)
    val countryId: String,
    val countryLogo: String,
    val countryName: String
)