package fr.uha.hassenforder.team.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.hassenforder.android.database.DatabaseTypeConverters
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.model.MoabDinoAssociation
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.model.TeamDinoAssociation

@Database(
    entities = [
        Dino::class,
        Team::class,
        TeamDinoAssociation::class,
        Moab::class,
        MoabDinoAssociation::class

    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class TeamDatabase : RoomDatabase() {

    abstract val dinoDao: DinoDao

    abstract val teamDao: TeamDao

    abstract val moabDao: MoabDao

    companion object {
        private lateinit var instance: TeamDatabase

        @Synchronized
        fun create(context: Context): TeamDatabase {
            instance = Room.databaseBuilder(context, TeamDatabase::class.java, "team.db").build()
            return instance
        }

        @Synchronized
        fun get(): TeamDatabase {
            return instance
        }

    }

}
