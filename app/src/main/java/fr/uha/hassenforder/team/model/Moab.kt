package fr.uha.hassenforder.team.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "moabs")
data class Moab(
    @PrimaryKey(autoGenerate = true)
    val mid: Long = 0,
    val name: String = "",
    val image: Uri? = null
)