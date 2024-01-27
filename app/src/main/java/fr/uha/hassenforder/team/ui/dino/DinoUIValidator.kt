package fr.uha.hassenforder.team.ui.dino

import android.net.Uri
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type

object DinoUIValidator {

    fun validateNameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            newValue.length > 12 ->  R.string.value_too_long
            else -> null
        }
    }

    fun validateGenderChange(newValue: Gender?) : Int? {
        return when {
            newValue == null ->  R.string.gender_must_set
            else -> null
        }
    }

    fun validateTypeChange(newValue: Type?) : Int? {
        return when {
            newValue == null ->  R.string.type_must_set
            else -> null
        }
    }

    fun validateRegimeChange(newValue: Regime?) : Int? {
        return when {
            newValue == null ->  R.string.regime_must_set
            else -> null
        }
    }

    fun validateApprivoiserChange(newValue: Apprivoiser?) : Int? {
        return when {
            newValue == null ->  R.string.apprivoiser_must_set
            else -> null
        }
    }

    fun validatePictureChange(newValue: Uri?) : Int? {
        return null
    }

}
