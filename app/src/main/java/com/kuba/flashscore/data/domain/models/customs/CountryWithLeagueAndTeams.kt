package com.kuba.flashscore.data.domain.models.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.Country
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryWithLeagueAndTeams(
    val country: Country,
    val leagueWithTeams: List<LeagueWithTeams>
) : Parcelable