package com.kuba.flashscore.data.network.mappers.event

import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.network.models.events.*
import com.kuba.flashscore.other.DomainMapper

class EventDtoMapper : DomainMapper<EventDto, EventEntity> {

    override fun mapToLocalModel(model: EventDto, foreignKey: String?): EventEntity {
        return EventEntity(
            leagueId = model.leagueId,
            matchAwayteamExtraScore = model.matchAwayteamExtraScore,
            matchAwayteamFtScore = model.matchAwayteamFtScore,
            matchAwayteamHalftimeScore = model.matchAwayteamHalftimeScore,
            matchAwayteamId = model.matchAwayteamId,
            matchAwayteamPenaltyScore = model.matchAwayteamPenaltyScore,
            matchAwayteamScore = model.matchAwayteamScore,
            matchAwayteamSystem = model.matchAwayteamSystem,
            matchDate = model.matchDate,
            matchHometeamExtraScore = model.matchHometeamExtraScore,
            matchHometeamFtScore = model.matchHometeamFtScore,
            matchHometeamHalftimeScore = model.matchHometeamHalftimeScore,
            matchHometeamId = model.matchHometeamId,
            matchHometeamPenaltyScore = model.matchHometeamPenaltyScore,
            matchHometeamScore = model.matchHometeamScore,
            matchHometeamSystem = model.matchHometeamSystem,
            matchId = model.matchId,
            matchLive = model.matchLive,
            matchReferee = model.matchReferee,
            matchRound = model.matchRound,
            matchStadium = model.matchStadium,
            matchStatus = model.matchStatus,
            matchTime = model.matchTime
        )
    }

    fun toLocalList(initial: List<EventDto>, foreignKey: String?): List<EventEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }

    fun mapStatisticToLocal(model: List<Statistic>, matchId: String): List<StatisticEntity> {
        val statisticsList = mutableListOf<StatisticEntity>()
        model.forEach { statistic ->
            statisticsList.add(
                StatisticEntity(matchId, statistic.away, statistic.home, statistic.type)
            )
        }
        return statisticsList
    }

    fun mapGoalscorerToLocal(model: List<Goalscorer>, matchId: String): List<GoalscorerEntity> {
        val goalscorersList = mutableListOf<GoalscorerEntity>()
        model.forEach { goalscorer ->
            var scorer = ""
            var assistantId = ""
            var whichTeam = false
            if (goalscorer.homeScorerId.isEmpty()) {
                scorer = goalscorer.awayScorerId
                assistantId = goalscorer.awayAssistId
                whichTeam = false
            } else {
                scorer = goalscorer.homeScorerId
                assistantId = goalscorer.homeAssistId
                whichTeam = true
            }
            goalscorersList.add(
                GoalscorerEntity(
                    matchId,
                    assistantId,
                    scorer,
                    whichTeam,
                    goalscorer.info,
                    goalscorer.score,
                    goalscorer.time
                )
            )
        }
        return goalscorersList
    }

    fun mapCardToLocal(model: List<Card>, matchId: String): List<CardEntity> {
        val cardsList = mutableListOf<CardEntity>()
        model.forEach { card ->
            val fault = if (card.awayFault.isEmpty()) card.homeFault else card.awayFault
            val whichTeam = card.awayFault.isEmpty()
            cardsList.add(
                CardEntity(matchId, fault, card.card, whichTeam, card.info, card.time)
            )
        }
        return cardsList
    }

    fun mapSubstitutionsToLocal(
        model: Substitutions,
        matchId: String
    ): List<SubstitutionsEntity> {
        val substitutionsList = mutableListOf<SubstitutionsEntity>()
        if (model.away.isNotEmpty()) {
            model.away.forEach { substitution ->
                substitutionsList.add(
                    SubstitutionsEntity(
                        matchId, substitution.substitution, substitution.time, false
                    )
                )
            }
        }
        if (model.home.isNotEmpty()) {
            model.home.forEach { substitution ->
                substitutionsList.add(
                    SubstitutionsEntity(
                        matchId, substitution.substitution, substitution.time, true
                    )
                )
            }
        }
        return substitutionsList
    }

    fun mapLineupToLocal(model: Lineup?, matchId: String): List<LineupEntity> {
        return mapHomeLineupToLocal(model, matchId) + mapAwayLineupToLocal(model, matchId)
    }

    private fun mapAwayLineupToLocal(
        model: Lineup?,
        matchId: String
    ): List<LineupEntity> {
        val lineupList = mutableListOf<LineupEntity>()
        model?.away?.apply {
            if (startingLineups.isNotEmpty()) {
                startingLineups.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = false,
                            starting = true,
                            missing = false,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }
            }
            if (missingPlayers.isNotEmpty()) {
                missingPlayers.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = false,
                            starting = false,
                            missing = true,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }

            }
            if (substitutes.isNotEmpty()) {
                substitutes.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = false,
                            starting = false,
                            missing = false,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }

            }
        }
        return lineupList
    }

    private fun mapHomeLineupToLocal(
        model: Lineup?,
        matchId: String
    ): List<LineupEntity> {
        val lineupList = mutableListOf<LineupEntity>()

        model?.home?.apply {
            if (startingLineups.isNotEmpty()) {
                startingLineups.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = true,
                            starting = true,
                            missing = false,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }
            }
            if (missingPlayers.isNotEmpty()) {
                missingPlayers.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = true,
                            starting = false,
                            missing = true,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }
            }
            if (!substitutes.isNullOrEmpty()) {
                substitutes.forEach { startingLineup ->
                    lineupList.add(
                        LineupEntity(
                            matchId,
                            startingLineup.lineupNumber,
                            startingLineup.lineupPosition,
                            whichTeam = true,
                            starting = false,
                            missing = false,
                            playerKey = startingLineup.playerKey
                        )
                    )
                }
            }
        }
        return lineupList
    }
}