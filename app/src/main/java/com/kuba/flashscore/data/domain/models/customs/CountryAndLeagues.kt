package com.kuba.flashscore.data.domain.models.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.League
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryAndLeagues(
    val country: Country,
    val leagues: List<League>
) : Parcelable