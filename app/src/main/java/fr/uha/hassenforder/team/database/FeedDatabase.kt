package fr.uha.hassenforder.team.database

import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Capacity
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.model.MoabDinoAssociation
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.model.TeamDinoAssociation
import fr.uha.hassenforder.team.model.Type
import java.util.Date
import java.util.Random


class FeedDatabase {

    private suspend fun feedDinos(): LongArray {
        val dao: DinoDao = TeamDatabase.get().dinoDao
        val ids = LongArray(4)
        ids[0] =
            dao.create(getRandomDino(Gender.MIXTE, Type.AERIEN, Regime.CARNIVORE, Apprivoiser.OUI))
        ids[1] =
            dao.create(getRandomDino(Gender.MIXTE, Type.AQUATIQUE, Regime.INCONNU, Apprivoiser.NON))
        ids[2] =
            dao.create(getRandomDino(Gender.NO, Type.CAVERNE, Regime.HERBIVORE, Apprivoiser.OUI))
        ids[3] = dao.create(
            getRandomDino(
                Gender.NO, Type.TERRESTRE, Regime.HERBIVORE, Apprivoiser.INCONNU
            )
        )
        return ids
    }


    private suspend fun feedTeams(pids: LongArray) {
        val dao: TeamDao = TeamDatabase.get().teamDao
        val team = getRandomTeam()
        val tid = dao.create(team)
        dao.addTeamDino(TeamDinoAssociation(tid, pids.get(0)))
        dao.addTeamDino(TeamDinoAssociation(tid, pids.get(3)))
    }

    private suspend fun feedMoabs(pids: LongArray) {
        val moabDao: MoabDao = TeamDatabase.get().moabDao
        val moab = getRandomMoab()
        val mid = moabDao.create(moab)
        moabDao.addMoabDino(MoabDinoAssociation(mid, pids[0]))
        moabDao.addMoabDino(MoabDinoAssociation(mid, pids[1]))
    }

    suspend fun populate() {
        val pids = feedDinos()
        feedTeams(pids)
        feedMoabs(pids)
    }

    suspend fun clear() {
        TeamDatabase.get().clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()
        private val dinoNames: Array<String> = arrayOf(
            "Dodo",
            "Trex",
            "Raptor",
            "Spino",
            "Bronto",
            "Stego",
            "Ankylo",
            "Argent",
            "Ptera",
            "Carno",
            "Mosa",
            "Quetz",
            "Giga",
            "Pachy",
            "Trike",
            "Parasaur",
            "Bary",
            "Diplo",
            "Allo",
            "Plesi",
            "Megalodon",
            "Sarco",
            "Therizino",
            "Yuty",
            "Oviraptor",
            "Griffin"
        )

        private val teamNames: Array<String> = arrayOf(
            "farm ressources", "defense", "attaque", "transport", "voyage"
        )

        private val moabNames: Array<String> = arrayOf(
            "The Island", "Ragnarok", "Extinction", "Aberration", "Center"
        )

        private fun geRandomName(names: Array<String>): String {
            return names.get(rnd.nextInt(names.size))
        }

        private fun getRandomName(): String {
            return geRandomName(dinoNames)
        }

        private fun getRandomBetween(low: Int, high: Int): Int {
            return rnd.nextInt(high - low) + low
        }

        private fun getRandomBetween(low: Double, high: Double): Double {
            return rnd.nextDouble() * (high - low) + low
        }

        private fun getRandomDino(
            gender: Gender, type: Type, regime: Regime, apprivoiser: Apprivoiser
        ): Dino {
            return Dino(
                0, getRandomName(), gender, type, regime, apprivoiser, null
            )
        }

        private fun getRandomTeamName(): String {
            return geRandomName(teamNames)
        }

        private fun getRandomMoabName(): String {
            return geRandomName(moabNames)
        }

        private fun getRandomTeam(): Team {
            val randomCapacity = Capacity.values()[rnd.nextInt(Capacity.values().size)]
            return Team(
                0, getRandomTeamName(), Date(), getRandomBetween(3, 9), capacity = randomCapacity
            )
        }

        private fun getRandomMoab(): Moab {
            return Moab(
                0, getRandomMoabName(), null
            )
        }
    }
}
