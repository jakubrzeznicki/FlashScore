package com.kuba.flashscore.data.domain.models.customs

import android.os.Parcelable
import com.kuba.flashscore.data.domain.models.Coach
import com.kuba.flashscore.data.domain.models.Player
import com.kuba.flashscore.data.domain.models.Team
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TeamWithPlayersAndCoach(
    val team: Team,
    val players: List<Player>,
    val coaches: List<Coach>
) : Parcelable