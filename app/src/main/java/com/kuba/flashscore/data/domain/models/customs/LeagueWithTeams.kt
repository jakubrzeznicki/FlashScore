package com.kuba.flashscore.data.domain.models.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.League
import com.kuba.flashscore.data.domain.models.Team
import kotlinx.android.parcel.Parcelize

@Parcelize
data class LeagueWithTeams(
    val league: League,
    val teams: List<Team>
) : Parcelable