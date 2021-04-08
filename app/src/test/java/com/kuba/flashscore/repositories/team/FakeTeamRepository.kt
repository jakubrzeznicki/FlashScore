package com.kuba.flashscore.repositories.team

import com.kuba.flashscore.data.domain.models.Country
import com.kuba.flashscore.data.domain.models.customs.CountryAndLeagues
import com.kuba.flashscore.data.domain.models.customs.CountryWithLeagueAndTeams
import com.kuba.flashscore.data.local.models.entities.*
import com.kuba.flashscore.data.local.models.entities.customs.CountryAndLeaguesEntity
import com.kuba.flashscore.data.local.models.entities.customs.CountryWithLeagueAndTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.LeagueWithTeamsEntity
import com.kuba.flashscore.data.local.models.entities.customs.TeamWithPlayersAndCoachEntity
import com.kuba.flashscore.other.Resource

class FakeTeamRepository : TeamRepository {

    private val teamItems = mutableListOf<TeamEntity>()
    private val coachItems = mutableListOf<CoachEntity>()
    private val playerItems = mutableListOf<PlayerEntity>()
    private val countryWithLeagueAndTeamsEntity  = CountryWithLeagueAndTeamsEntity(
        CountryEntity("countryId1", "countryLogo1", "countryName1"),
        listOf(
            LeagueWithTeamsEntity(
                LeagueEntity(
                    "countryId1",
                    "leagueId1",
                    "leagueLogo1",
                    "leagueName1",
                    "leagueSeason1"
                ),
                teamItems
            )
        )
    )


    private val teamWithPlayersAndCoachEntity  = TeamWithPlayersAndCoachEntity(
        TeamEntity(
            "leagueId1",
            "teamBadge2",
            "teamId2",
            "teamName2"
        ),
        playerItems,
        coachItems
    )

    private var shouldReturnNetworkError = false

    fun setShouldReturnNetworkError(value: Boolean) {
        shouldReturnNetworkError = value
    }

    override suspend fun insertCoaches(coaches: List<CoachEntity>) {
        coachItems.addAll(coaches)
    }

    override suspend fun insertPlayers(players: List<PlayerEntity>) {
        playerItems.addAll(players)
    }

    override suspend fun insertTeams(teams: List<TeamEntity>) {
        teamItems.addAll(teams)
    }

    override suspend fun refreshTeamsFromSpecificLeague(leagueId: String): Resource<CountryWithLeagueAndTeams> {
        return if (shouldReturnNetworkError) {
            Resource.error("Error", null)
        } else {
            Resource.success(countryWithLeagueAndTeamsEntity.asDomainModel())
        }
    }

    override suspend fun getTeamsWithLeagueAndCountryInformationFromLeagueFromDb(leagueId: String): CountryWithLeagueAndTeamsEntity {
        return countryWithLeagueAndTeamsEntity
    }

    override suspend fun getTeamWithPlayersAndCoachFromDb(teamId: String): TeamWithPlayersAndCoachEntity {
        return teamWithPlayersAndCoachEntity
    }

//    private fun producePlayerEntities(): List<PlayerEntity> {
//        return listOf(
//            PlayerEntity(
//                "teamId2",
//                "playerAge1",
//                "playerCountry1",
//                "playerGoals1",
//                1L,
//                "playerMatchPlayed1",
//                "playerName1",
//                "playerNumber1",
//                "playerRedCard1",
//                "playerType1",
//                "playerYellowCard1"
//            )
//        )
//    }
//
//    private fun produceCoachEntities(): List<CoachEntity> {
//        return listOf(
//            CoachEntity(
//                "teamId2",
//                "coachAge1",
//                "coachCountry1",
//                "coachName1"
//            )
//        )
//    }
}
