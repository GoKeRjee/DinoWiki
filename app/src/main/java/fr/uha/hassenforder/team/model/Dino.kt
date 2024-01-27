package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "dinos")
data class Dino(
    @PrimaryKey(autoGenerate = true)
    val pid: Long = 0,
    val name: String,
    val gender: Gender,
    val type: Type,
    val regime: Regime,
    val apprivoiser: Apprivoiser
    //val picture : Uri? = null
)