package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.android.database.DeltaUtil
import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.database.TeamDao
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.model.TeamDinoAssociation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class TeamRepository(
    private val teamDao: TeamDao, private val dinoDao: DinoDao
) {
    fun getAll(): Flow<List<Team>> {
        return teamDao.getAll()
    }

    fun getTeamById(id: Long): Flow<FullTeam?> {
        return teamDao.getTeamById(id)
    }

    fun getDinoById(id: Long): Flow<Dino?> {
        return dinoDao.getDinoById(id)
    }


    @WorkerThread
    suspend fun createTeam(team: Team): Long {
        return withContext(Dispatchers.IO) {
            teamDao.upsert(team)
        }
    }

    @WorkerThread
    suspend fun saveTeam(oldTeam: FullTeam, newTeam: FullTeam): Long {
        return withContext(Dispatchers.IO) {
            var teamToSave: Team? = null
            if (!Comparators.shallowEqualsTeam(oldTeam.team, newTeam.team)) {
                teamToSave = newTeam.team
            }
            val teamId: Long = newTeam.team.tid
            val delta: DeltaUtil<Dino, TeamDinoAssociation> =
                object : DeltaUtil<Dino, TeamDinoAssociation>() {
                    override fun getId(input: Dino): Long {
                        return input.pid
                    }

                    override fun same(initial: Dino, now: Dino): Boolean {
                        return true
                    }

                    override fun createFor(input: Dino): TeamDinoAssociation {
                        return TeamDinoAssociation(teamId, input.pid)
                    }
                }
            val oldList = oldTeam.members
            val newList = newTeam.members
            delta.calculate(oldList, newList)

            if (teamToSave != null) teamDao.upsert(teamToSave)
            teamDao.removeTeamDino(delta.toRemove)
            teamDao.addTeamDino(delta.toAdd)

            return@withContext teamId
        }
    }

    suspend fun delete(team: Team) {
        teamDao.delete(team)
        teamDao.deleteTeamDinos(team.tid)
    }
}
