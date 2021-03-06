package com.kuba.flashscore.data.network.mappers

import com.kuba.flashscore.data.local.models.entities.PlayerEntity
import com.kuba.flashscore.data.network.models.PlayerDto
import com.kuba.flashscore.other.DomainMapper

class PlayerDtoMapper : DomainMapper<PlayerDto, PlayerEntity> {
    override fun mapToLocalModel(model: PlayerDto, foreignKey: String?): PlayerEntity {
        return PlayerEntity(
            teamId = foreignKey,
            playerAge = model.playerAge,
            playerCountry = model.playerCountry,
            playerGoals = model.playerGoals,
            playerKey = model.playerKey,
            playerMatchPlayed = model.playerMatchPlayed,
            playerName = model.playerName,
            playerNumber = model.playerNumber,
            playerRedCards = model.playerRedCards,
            playerType = model.playerType,
            playerYellowCards = model.playerYellowCards
        )
    }

    fun toLocalList(initial: List<PlayerDto>, foreignKey: String?): List<PlayerEntity> {
        return initial.map { mapToLocalModel(it, foreignKey) }
    }

}