package com.kuba.flashscore.data.local.entities.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.kuba.flashscore.data.local.entities.Country
import com.kuba.flashscore.data.local.entities.League

data class SchoolWithStudents(
    @Embedded val country: Country,
    @Relation(
        parentColumn = "countryId",
        entityColumn = "countryId"
    )
    val leagues: List<League>
)