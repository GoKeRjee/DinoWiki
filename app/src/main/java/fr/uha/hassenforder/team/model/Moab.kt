package fr.uha.hassenforder.team.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "moabs")
data class Moab(
    @PrimaryKey(autoGenerate = true)
    val mid: Long = 0,
    val name: String = "",
    val startDay: Date = Date(),
    val community: Community = Community.NO,
    val image: Uri? = null
)