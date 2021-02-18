package com.kuba.flashscore.local.models.incident

data class IncidentItem(
    val firstName: String,
    val secondName: String,
    val time: String,
    val INCIDENTTYPE: INCIDENTTYPE,
    val whatTeam: Boolean,
    val score: String? = null
) : Incident

enum class INCIDENTTYPE {
    GOAL,
    SUBSTITUTION,
    YELLOW_CARD,
    RED_CARD
}