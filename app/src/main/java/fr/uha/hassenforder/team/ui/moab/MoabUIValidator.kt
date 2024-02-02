package fr.uha.hassenforder.team.ui.moab

import android.net.Uri
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Dino

object MoabUIValidator {

    fun validateNameChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            newValue.length > 12 -> R.string.value_too_long
            else -> null
        }
    }

    fun validatePictureChange(newValue: Uri?): Int? {
        return null
    }

    fun validateMembersChange(state: MoabViewModel.MoabUIState, newValue: List<Dino>?): Int? {
        if (newValue == null) return R.string.members_not_empty
        val size = newValue.size
        return when {
            size == 0 -> R.string.members_not_empty
            size < 3 -> R.string.members_not_enough
            size > 6 -> R.string.members_too_much
            else -> null
        }
    }
}