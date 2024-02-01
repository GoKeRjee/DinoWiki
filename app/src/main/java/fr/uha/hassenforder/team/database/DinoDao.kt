package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.DinoWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface DinoDao {

    @Query("SELECT * FROM dinos")
    fun getAll(): Flow<List<Dino>>

    @Query(
        "SELECT * " +
                //", (SELECT COUNT(*) FROM teams T WHERE T.leaderId = P.pid) AS leaderCount" +
                ", (SELECT COUNT(*) FROM tpas TPA WHERE TPA.pid = P.pid) AS memberCount" +
                " FROM dinos AS P"
    )
    fun getAllWithDetails(): Flow<List<DinoWithDetails>>

    @Query("SELECT * FROM dinos WHERE pid = :id")
    fun getDinoById(id: Long): Flow<Dino?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(dino: Dino): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(dino: Dino): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(dino: Dino): Long

    @Delete
    fun delete(dino: Dino)
}
