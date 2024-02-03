package fr.uha.hassenforder.team.model

class Comparators {

    companion object {

        fun shallowEqualsDino(oldDino: Dino?, newDino: Dino?): Boolean {
            if (oldDino == null && newDino == null) return true
            if (oldDino != null && newDino == null) return false
            if (oldDino == null && newDino != null) return false
            oldDino as Dino
            newDino as Dino
            if (oldDino.pid != newDino.pid) return false
            return true
        }

        fun shallowEqualsListDinos(oldDinos: List<Dino>?, newDinos: List<Dino>?): Boolean {
            if (oldDinos == null && newDinos == null) return true
            if (oldDinos != null && newDinos == null) return false
            if (oldDinos == null && newDinos != null) return false
            oldDinos as List<Dino>
            newDinos as List<Dino>
            if (oldDinos.size != newDinos.size) return false
            val oldMap = mutableSetOf<Long>()
            oldDinos.forEach { p -> oldMap.add(p.pid) }
            newDinos.forEach { p -> if (!oldMap.contains(p.pid)) return false }
            val newMap = mutableSetOf<Long>()
            newDinos.forEach { p -> newMap.add(p.pid) }
            oldDinos.forEach { p -> if (!newMap.contains(p.pid)) return false }
            return true
        }

        fun shallowEqualsTeam(oldTeam: Team?, newTeam: Team?): Boolean {
            if (newTeam == null && oldTeam == null) return true
            if (newTeam != null && oldTeam == null) return false
            if (newTeam == null && oldTeam != null) return false
            val safeNew: Team = newTeam as Team
            val safeOld: Team = oldTeam as Team
            if (safeNew.tid != safeOld.tid) return false
            if (safeNew.name != safeOld.name) return false
            if (safeNew.startDay != safeOld.startDay) return false
            if (safeNew.duration != safeOld.duration) return false
            if (safeNew.capacity != safeOld.capacity) return false
            return true
        }

        fun shallowEqualsMoab(oldMoab: Moab?, newMoab: Moab?): Boolean {
            if (newMoab == null && oldMoab == null) return true
            if (newMoab != null && oldMoab == null) return false
            if (newMoab == null && oldMoab != null) return false
            val safeNew: Moab = newMoab as Moab
            val safeOld: Moab = oldMoab as Moab
            if (safeNew.mid != safeOld.mid) return false
            if (safeNew.name != safeOld.name) return false
            if (safeNew.startDay != safeOld.startDay) return false
            if (safeNew.community != safeOld.community) return false
            if (safeNew.image != safeOld.image) return false
            return true
        }
    }
}
