package com.kuba.flashscore.data.local.util

interface LocalMapper <T, LocalModel>{

    fun mapToLocalModel(model: T): LocalModel

    fun mapFromLocalModel(localModel: LocalModel): T
}