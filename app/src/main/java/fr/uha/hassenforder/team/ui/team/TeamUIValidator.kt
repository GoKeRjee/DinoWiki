package fr.uha.hassenforder.team.ui.team

import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Capacity
import fr.uha.hassenforder.team.model.Dino
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

object TeamUIValidator {

    fun validateNameChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            else -> null
        }
    }

    private fun instantToMidnight(date: Date): Instant {
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        return calendar.toInstant()
    }

    fun validateStartDayChange(newValue: Date?): Int? {
        if (newValue == null) return R.string.date_must_set
        val day = instantToMidnight(newValue)
        val today = instantToMidnight(Date())
        val between: Long = ChronoUnit.DAYS.between(today, day)
        if (between < -7) return R.string.date_too_old
        if (between > 7) return R.string.date_too_far
        return null
    }

    fun validateDurationChange(newValue: Int): Int? {
        return when {
            newValue < 2 -> R.string.duration_too_short
            newValue > 9 -> R.string.duration_too_long
            else -> null
        }
    }

    fun validateMembersChange(state: TeamViewModel.TeamUIState, newValue: List<Dino>?): Int? {
        if (newValue == null) return R.string.members_not_empty
        val size = newValue.size
        return when {
            size == 0 -> R.string.members_not_empty
            size < 3 -> R.string.members_not_enough
            size > 6 -> R.string.members_too_much
            else -> null
        }
    }

    fun validateCapacityChange(newValue: Capacity?): Int? {
        return if (newValue == null) {
            R.string.capacity_must_set
        } else {
            null
        }
    }

}
