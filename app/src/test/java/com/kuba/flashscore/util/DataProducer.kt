package com.kuba.flashscore.util

import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.*
import com.kuba.flashscore.data.local.models.entities.event.customs.CountryWithLeagueWithEventsAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.EventWithEventInformationEntity
import com.kuba.flashscore.data.local.models.entities.event.customs.LeagueWithEventsEntity

object DataProducer {

    fun produceCountryEntity(itemNumber: Int) =
        CountryEntity("countryId$itemNumber", "countryLogo$itemNumber", "countryName$itemNumber")

    fun produceLeagueEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        LeagueEntity(
            "countryId$itemNumberForeignKey",
            "leagueId$itemNumber",
            "leagueLogo$itemNumber",
            "leagueName$itemNumber",
            "leagueSeason$itemNumber"
        )

    fun producePlayerEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        PlayerEntity(
            "teamId$itemNumberForeignKey",
            "playerAge$itemNumber",
            "playerCountry$itemNumber",
            "playerGoals$itemNumber",
            itemNumber.toLong(),
            "playerMatchPlayed$itemNumber",
            "playerName$itemNumber",
            "playerNumber$itemNumber",
            "playerRedCard$itemNumber",
            "playerType$itemNumber",
            "playerYellowCard$itemNumber"
        )

    fun produceCoachEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        CoachEntity(
            "teamId$itemNumberForeignKey",
            "coachAge$itemNumber",
            "coachCountry$itemNumber",
            "coachName$itemNumber"
        )

    fun produceTeamEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        TeamEntity(
            "leagueId$itemNumberForeignKey",
            "teamBadge$itemNumber",
            "teamId$itemNumber",
            "teamName$itemNumber"
        )

    fun produceStandingEntity(
        itemNumber: Int,
        itemNumberForeignKeyLeague: Int,
        itemNumberForeignKeyTeam: Int,
        standingType: StandingType
    ) =
        StandingEntity(
            "leagueRound$itemNumber",
            "leagueD$itemNumber",
            "leagueGA$itemNumber",
            "leagueGF$itemNumber",
            "leagueL$itemNumber",
            "leaguePTS$itemNumber",
            "leaguePayed$itemNumber",
            "leaguePosition$itemNumber",
            "leagueW$itemNumber",
            "promotion$itemNumber",
            "teamId$itemNumberForeignKeyTeam",
            "leagueId$itemNumberForeignKeyLeague",
            standingType
        )

    fun produceCardEntity(itemNumber: Int, itemNumberForeignKey: Int, whichTeam: Boolean) =
        CardEntity(
            "matchId$itemNumberForeignKey",
            "fault$itemNumber",
            "card$itemNumber",
            whichTeam,
            "info$itemNumber",
            "time$itemNumber"
        )

    fun produceGoalscorerEntity(itemNumber: Int, itemNumberForeignKey: Int, whichTeam: Boolean) =
        GoalscorerEntity(
            "matchId$itemNumberForeignKey",
            "assistId$itemNumber",
            "scorerId$itemNumber",
            whichTeam,
            "info$itemNumber",
            "score$itemNumber",
            "time$itemNumber"
        )

    fun produceLineupEntity(itemNumber: Int, itemNumberForeignKey: Int, whichTeam: Boolean) =
        LineupEntity(
            "matchId$itemNumberForeignKey",
            "lineupNumber$itemNumber",
            "lineupPosition$itemNumber",
            whichTeam = whichTeam,
            starting = true,
            missing = false,
            playerKey = "playerKey$itemNumber"
        )


    fun produceStatisticEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        StatisticEntity(
            "matchId$itemNumberForeignKey",
            "away$itemNumber",
            "home$itemNumber",
            "type$itemNumber"
        )

    fun produceSubstitutionEntity(itemNumber: Int, itemNumberForeignKey: Int, whichTeam: Boolean) =
        SubstitutionsEntity(
            "matchId$itemNumberForeignKey",
            "substitutions$itemNumber",
            "time$itemNumber",
            whichTeam
        )

    fun produceEventEntity(itemNumber: Int, itemNumberForeignKey: Int) =
        EventEntity(
            "leagueId$itemNumberForeignKey",
            "matchDate$itemNumberForeignKey",
            "matchId$itemNumber",
            "matchLive$itemNumber",
            "matchReferee$itemNumber",
            "matchRound$itemNumber",
            "matchStadium$itemNumber",
            "matchStatus$itemNumber",
            "matchTime$itemNumber"
        )

    fun produceEventInformationEntity(
        itemNumber: Int,
        itemNumberForeignKeyMatch: Int,
        itemNumberForeignKeyTeam: Int,
        isHome: Boolean
    ) =
        EventInformationEntity(
            "matchId$itemNumberForeignKeyMatch",
            "extraScore$itemNumber",
            "fullTimeScore$itemNumber",
            "halfTimeScore$itemNumber",
            "teamId$itemNumberForeignKeyTeam",
            "teamPenaltyScore$itemNumber",
            "teamScore$itemNumber",
            "teamSystem$itemNumber",
            isHome
        )

     fun produceCountryWithLeagueWithEventsAndTeamsEntity(): CountryWithLeagueWithEventsAndTeamsEntity {
        return CountryWithLeagueWithEventsAndTeamsEntity(
            produceCountryEntity(1),
            listOf(
                LeagueWithTeamsEntity(
                    produceLeagueEntity(1, 1),
                    listOf(produceTeamEntity(1, 1))
                )
            ),
            listOf(
                LeagueWithEventsEntity(
                    produceLeagueEntity(1, 1),
                    listOf(
                        EventWithEventInformationEntity(
                            produceEventEntity(1, 1),
                            listOf(
                                produceEventInformationEntity(1, 1, 1, true),
                                produceEventInformationEntity(2, 1, 2, true)
                            )
                        )
                    )
                )
            )
        )
    }

     fun produceEventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(): EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity {
        return EventWithCardsAndGoalscorersAndLineupsAndStatisticsAnSubstitutionsEntity(
            produceEventEntity(1, 1),
            listOf(produceCardEntity(1, 1, true)),
            listOf(produceGoalscorerEntity(1, 1, true)),
            listOf(produceLineupEntity(1, 1, true)),
            listOf(produceStatisticEntity(1, 1)),
            listOf(produceSubstitutionEntity(1, 1, true))
        )
    }
}