package com.kuba.flashscore.local.models.entities

import android.os.Parcelable
import android.text.format.DateUtils
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.kuba.flashscore.other.DateUtils.createTimestamp
import com.kuba.flashscore.other.DateUtils.dateToLong
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "countries_table")
data class CountryEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "country_id")
    val countryId: String,
    @ColumnInfo(name = "country_logo")
    val countryLogo: String,
    @ColumnInfo(name = "country_name")
    val countryName: String,
    @ColumnInfo(name = "date_cached")
    val dateCached: Long = dateToLong(
        createTimestamp()
    )
) : Parcelable