package fr.uha.hassenforder.team.model

import androidx.room.Embedded

class DinoWithDetails(
    @Embedded
    val dino: Dino,

    val memberCount: Int,
)
