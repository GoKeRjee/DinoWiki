package fr.uha.hassenforder.team.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullTeam(
    @Embedded
    val team: Team,

    @Relation(
        parentColumn = "tid",
        entityColumn = "pid",
        associateBy = Junction(TeamDinoAssociation::class)
    )
    val members: List<Dino>,
)
