package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.DinoWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext


class DinoRepository(private val dinoDao: DinoDao) {
    fun getAll(): Flow<List<DinoWithDetails>> {
        return dinoDao.getAllWithDetails()
    }

    fun getDinoById(id: Long): Flow<Dino?> {
        return dinoDao.getDinoById(id)
    }

    suspend fun create(dino: Dino): Long = withContext(Dispatchers.IO) {
        return@withContext dinoDao.create(dino)
    }

    suspend fun update(oldDino: Dino, dino: Dino): Long = withContext(Dispatchers.IO) {
        return@withContext dinoDao.update(dino)
    }

    suspend fun upsert(dino: Dino): Long = withContext(Dispatchers.IO) {
        return@withContext dinoDao.upsert(dino)
    }

    suspend fun delete(dino: Dino) = withContext(Dispatchers.IO) {
        dinoDao.delete(dino)
    }

}
