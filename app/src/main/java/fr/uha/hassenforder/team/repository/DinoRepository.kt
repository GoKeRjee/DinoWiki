package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.model.Dino
import kotlinx.coroutines.flow.Flow

class DinoRepository(private val dinoDao: DinoDao) {
    fun getAll(): Flow<List<Dino>> {
        return dinoDao.getAll()
    }

    fun getDinoById(id: Long): Flow<Dino?> {
        return dinoDao.getDinoById(id)
    }

    fun create(dino: Dino): Long {
        return dinoDao.create(dino)
    }

    fun update(dino: Dino): Long {
        return dinoDao.update(dino)
    }

    fun upsert(dino: Dino): Long {
        return dinoDao.upsert(dino)
    }

    fun delete(dino: Dino) {
        dinoDao.delete(dino)
    }

}
