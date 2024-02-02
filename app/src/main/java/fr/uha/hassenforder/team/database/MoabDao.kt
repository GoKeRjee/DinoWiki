package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.hassenforder.team.model.FullMoab
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.model.MoabDinoAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface MoabDao {

    @Query("SELECT * FROM moabs")
    fun getAll(): Flow<List<Moab>>

    @Query("SELECT * FROM moabs WHERE mid = :id")
    @Transaction
    fun getMoabById(id: Long): Flow<FullMoab?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(moab: Moab): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(moab: Moab): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(moab: Moab): Long

    @Delete
    fun delete(moab: Moab)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoabDino(member: MoabDinoAssociation)

    @Delete
    suspend fun removeMoabDino(member: MoabDinoAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addMoabDino(members: List<MoabDinoAssociation>)

    @Delete
    suspend fun removeMoabDino(members: List<MoabDinoAssociation>)

    @Query("DELETE FROM moab_dino_association WHERE mid = :mid")
    fun deleteMoabDinos(mid: Long)

}
