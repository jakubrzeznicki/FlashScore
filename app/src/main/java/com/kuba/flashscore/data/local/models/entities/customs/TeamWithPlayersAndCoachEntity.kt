package com.kuba.flashscore.data.local.models.entities.customs

import androidx.room.Embedded
import androidx.room.Relation
import com.kuba.flashscore.data.domain.models.customs.LeagueWithTeams
import com.kuba.flashscore.data.domain.models.customs.TeamWithPlayersAndCoach
import com.kuba.flashscore.data.local.models.entities.CoachEntity
import com.kuba.flashscore.data.local.models.entities.PlayerEntity
import com.kuba.flashscore.data.local.models.entities.TeamEntity
import timber.log.Timber

data class TeamWithPlayersAndCoachEntity(
    @Embedded
    val team: TeamEntity,

    @Relation(parentColumn = "team_id", entityColumn = "player_team_id")
    val players: List<PlayerEntity> = emptyList(),

    @Relation(parentColumn = "team_id", entityColumn = "coach_team_id")
    val coaches: List<CoachEntity> = emptyList()
) {
    fun asDomainModel(): TeamWithPlayersAndCoach {
        return TeamWithPlayersAndCoach(
            team.asDomainModel(),
            players.map { it.asDomainModel() },
            coaches.map { it.asDomainModel() }
        )
    }
}