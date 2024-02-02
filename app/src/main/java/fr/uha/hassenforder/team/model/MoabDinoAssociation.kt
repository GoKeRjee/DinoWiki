package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.Index

@Entity(
    tableName = "moab_dino_association",
    primaryKeys = ["mid", "pid"],
    indices = [Index("mid"), Index("pid")]
)
data class MoabDinoAssociation(
    val mid: Long,
    val pid: Long
)