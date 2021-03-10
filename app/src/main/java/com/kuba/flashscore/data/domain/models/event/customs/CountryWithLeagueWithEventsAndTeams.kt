package com.kuba.flashscore.data.domain.models.event.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CountryWithLeagueWithEventsAndTeams(
    val country: Country,
    val leagueWithTeams: List<LeagueWithTeams>,
    val leagueWithEvents: List<LeagueWithEvents>
) : Parcelable