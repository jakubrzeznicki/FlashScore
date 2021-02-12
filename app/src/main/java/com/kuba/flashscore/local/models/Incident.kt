package com.kuba.flashscore.local.models

data class Incident(
    val firstName: String,
    val secondName: String,
    val time: String,
    val INCIDENTTYPE: INCIDENTTYPE,
    val whatTeam: Boolean
)

enum class INCIDENTTYPE {
    GOAL,
    SUBSTITUTION,
    YELLOW_CARD,
    RED_CARD
}