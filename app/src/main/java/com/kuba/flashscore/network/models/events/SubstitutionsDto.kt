package com.kuba.flashscore.network.models.events

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class SubstitutionsDto(
    val awayDto: List<AwayXDto>,
    val home: List<HomeXDto>
) : Parcelable