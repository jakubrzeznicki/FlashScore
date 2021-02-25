package com.kuba.flashscore.other

interface DomainMapper <T, LocalModel>{

    fun mapToLocalModel(model: T, foreignKey: String?): LocalModel
}