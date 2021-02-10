package com.kuba.flashscore.network.models.events

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


@Parcelize
data class LineupDto(
    val awayDto: AwayDto,
    val homeDto: HomeDto
) : Parcelable