package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.android.database.DeltaUtil
import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.database.MoabDao
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.FullMoab
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.model.MoabDinoAssociation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MoabRepository(
    private val moabDao: MoabDao, private val dinoDao: DinoDao
) {
    fun getAll(): Flow<List<Moab>> {
        return moabDao.getAll()
    }

    fun getMoabById(id: Long): Flow<FullMoab?> {
        return moabDao.getMoabById(id)
    }

    fun getDinoById(id: Long): Flow<Dino?> {
        return dinoDao.getDinoById(id)
    }


    @WorkerThread
    suspend fun createMoab(moab: Moab): Long {
        return withContext(Dispatchers.IO) {
            moabDao.upsert(moab)
        }
    }

    @WorkerThread
    suspend fun saveMoab(oldMoab: FullMoab, newMoab: FullMoab): Long {
        return withContext(Dispatchers.IO) {
            var moabToSave: Moab? = null
            if (!Comparators.shallowEqualsMoab(oldMoab.moab, newMoab.moab)) {
                moabToSave = newMoab.moab
            }
            val moabId: Long = newMoab.moab.mid
            val delta: DeltaUtil<Dino, MoabDinoAssociation> =
                object : DeltaUtil<Dino, MoabDinoAssociation>() {
                    override fun getId(input: Dino): Long {
                        return input.pid
                    }

                    override fun same(initial: Dino, now: Dino): Boolean {
                        return true
                    }

                    override fun createFor(input: Dino): MoabDinoAssociation {
                        return MoabDinoAssociation(moabId, input.pid)
                    }
                }
            val oldList = oldMoab.members
            val newList = newMoab.members
            delta.calculate(oldList, newList)

            if (moabToSave != null) moabDao.upsert(moabToSave)
            moabDao.removeMoabDino(delta.toRemove)
            moabDao.addMoabDino(delta.toAdd)

            return@withContext moabId
        }
    }

    suspend fun delete(moab: Moab) {
        withContext(Dispatchers.IO) {
            moabDao.delete(moab)
            moabDao.deleteMoabDinos(moab.mid)
        }
    }
}
