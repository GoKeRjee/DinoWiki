package fr.uha.hassenforder.team.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullMoab(
    @Embedded
    val moab: Moab,

    @Relation(
        parentColumn = "mid",
        entityColumn = "pid",
        associateBy = Junction(MoabDinoAssociation::class)
    )
    val members: List<Dino>,
)
