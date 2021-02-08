package com.kuba.flashscore.network.mappers


import com.kuba.flashscore.data.local.entities.Player
import com.kuba.flashscore.network.models.PlayerDto



import com.kuba.flashscore.data.local.util.LocalMapper


class PlayerDtoMapper : LocalMapper<PlayerDto, Player> {


    override fun mapToLocalModel(model: PlayerDto): Player {
        return Player(
            playerKey = model.playerKey,
            playerAge = model.playerAge,
            playerCountry = model.playerCountry,
            playerGoals = model.playerGoals,
            playerMatchPlayed = model.playerMatchPlayed,
            playerName = model.playerName,
            playerNumber = model.playerNumber,
            playerRedCards = model.playerRedCards,
            playerType = model.playerType,
            playerYellowCards = model.playerYellowCards,
        )
    }

    override fun mapFromLocalModel(localModel: Player): PlayerDto {
        return PlayerDto(
            playerKey = localModel.playerKey,
            playerAge = localModel.playerAge,
            playerCountry = localModel.playerCountry,
            playerGoals = localModel.playerGoals,
            playerMatchPlayed = localModel.playerMatchPlayed,
            playerName = localModel.playerName,
            playerNumber = localModel.playerNumber,
            playerRedCards = localModel.playerRedCards,
            playerType = localModel.playerType,
            playerYellowCards = localModel.playerYellowCards,
        )
    }


    fun toLocalList(initial: List<PlayerDto>): List<Player> {
        return initial.map { mapToLocalModel(it) }
    }

    fun fromLocalList(initial: List<Player>): List<PlayerDto> {
        return initial.map { mapFromLocalModel(it) }
    }


}