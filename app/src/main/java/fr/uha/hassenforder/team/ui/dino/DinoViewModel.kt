package fr.uha.hassenforder.team.ui.dino

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.kotlin.combine
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type
import fr.uha.hassenforder.team.repository.DinoRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject


@HiltViewModel
class DinoViewModel @Inject constructor(
    private val repository: DinoRepository
) : ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface DinoState {
        data class Success(val dino: Dino) : DinoState
        object Loading : DinoState
        object Error : DinoState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildName(state: DinoUIState, newValue: String): FieldWrapper<String> {
                val errorId: Int? = DinoUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildGender(state: DinoUIState, newValue: Gender?): FieldWrapper<Gender?> {
                val errorId: Int? = DinoUIValidator.validateGenderChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildType(state: DinoUIState, newValue: Type?): FieldWrapper<Type?> {
                val errorId: Int? = DinoUIValidator.validateTypeChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildRegime(state: DinoUIState, newValue: Regime?): FieldWrapper<Regime?> {
                val errorId: Int? = DinoUIValidator.validateRegimeChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildApprivoiser(
                state: DinoUIState,
                newValue: Apprivoiser?
            ): FieldWrapper<Apprivoiser?> {
                val errorId: Int? = DinoUIValidator.validateApprivoiserChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildPicture(state: DinoUIState, newValue: Uri?): FieldWrapper<Uri?> {
                val errorId: Int? = DinoUIValidator.validatePictureChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _genderState = MutableStateFlow(FieldWrapper<Gender?>())
    private val _typeState = MutableStateFlow(FieldWrapper<Type?>())
    private val _regimeState = MutableStateFlow(FieldWrapper<Regime?>())
    private val _apprivoiserState = MutableStateFlow(FieldWrapper<Apprivoiser?>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri?>())

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialDinoState: StateFlow<DinoState> = _id
        .flatMapLatest { id -> repository.getDinoById(id) }
        .map { p ->
            if (p != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, p.name))
                _genderState.emit(FieldWrapper.buildGender(uiState.value, p.gender))
                _typeState.emit(FieldWrapper.buildType(uiState.value, p.type))
                _regimeState.emit(FieldWrapper.buildRegime(uiState.value, p.regime))
                _apprivoiserState.emit(FieldWrapper.buildApprivoiser(uiState.value, p.apprivoiser))
//                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, p.picture))
                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, null))
                DinoState.Success(dino = p)
            } else {
                DinoState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DinoState.Loading)

    data class DinoUIState(
        val initialState: DinoState,
        val nameState: FieldWrapper<String>,
        val genderState: FieldWrapper<Gender?>,
        val typeState: FieldWrapper<Type?>,
        val regimeState: FieldWrapper<Regime?>,
        val apprivoiserState: FieldWrapper<Apprivoiser?>,
        val pictureState: FieldWrapper<Uri?>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is DinoState.Success) return null
            if (nameState.current != initialState.dino.name) return true
            if (genderState.current != initialState.dino.gender) return true
            if (typeState.current != initialState.dino.type) return true
            if (regimeState.current != initialState.dino.regime) return true
            if (apprivoiserState.current != initialState.dino.apprivoiser) return true
//            if (pictureState.current != initialState.person.picture) return true
            if (pictureState.current != null) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (nameState.errorId != null) return true
            if (genderState.errorId != null) return true
            if (typeState.errorId != null) return true
            if (regimeState.errorId != null) return true
            if (apprivoiserState.errorId != null) return true
            if (pictureState.errorId != null) return true
            return false
        }

        fun isModified(): Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable(): Boolean {
            val hasError = _hasError()
            if (hasError == null) return false
            val isModified = _isModified()
            if (isModified == null) return false
            return !hasError && isModified
        }
    }

    val uiState: StateFlow<DinoUIState> = combine(
        _initialDinoState,
        _nameState,
        _genderState,
        _typeState,
        _regimeState,
        _apprivoiserState,
        _pictureState
    ) { i, n, g, t, r, a, p -> DinoUIState(i, n, g, t, r, a, p) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DinoUIState(
            DinoState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class GenderChanged(val newValue: Gender) : UIEvent()
        data class TypeChanged(val newValue: Type) : UIEvent()
        data class RegimeChanged(val newValue: Regime) : UIEvent()
        data class ApprivoiserChanged(val newValue: Apprivoiser) : UIEvent()
        data class PictureChanged(val newValue: Uri?) : UIEvent()
    }

    data class DinoUICallback(
        val onEvent: (UIEvent) -> Unit,
    )

    val uiCallback = DinoUICallback(
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.NameChanged -> _nameState.emit(
                        FieldWrapper.buildName(
                            uiState.value,
                            it.newValue
                        )
                    )

                    is UIEvent.GenderChanged -> _genderState.emit(
                        FieldWrapper.buildGender(
                            uiState.value,
                            it.newValue
                        )
                    )

                    is UIEvent.TypeChanged -> _typeState.emit(
                        FieldWrapper.buildType(
                            uiState.value,
                            it.newValue
                        )
                    )

                    is UIEvent.RegimeChanged -> _regimeState.emit(
                        FieldWrapper.buildRegime(
                            uiState.value,
                            it.newValue
                        )
                    )

                    is UIEvent.ApprivoiserChanged -> _apprivoiserState.emit(
                        FieldWrapper.buildApprivoiser(
                            uiState.value,
                            it.newValue
                        )
                    )

                    is UIEvent.PictureChanged -> _pictureState.emit(
                        FieldWrapper.buildPicture(
                            uiState.value,
                            it.newValue
                        )
                    )
                }
            }
        }
    )

    fun edit(pid: Long) = viewModelScope.launch {
        _id.emit(pid)
    }

    fun create(dino: Dino) = viewModelScope.launch {
        val pid: Long = repository.create(dino)
        _id.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialDinoState.value !is DinoState.Success) return@launch
        val oldDino = _initialDinoState.value as DinoState.Success
        val dino = Dino(
            _id.value,
            _nameState.value.current!!,
            _genderState.value.current!!,
            _typeState.value.current!!,
            _regimeState.value.current!!,
            _apprivoiserState.value.current!!,
            //            _pictureState.value.current
        )
        repository.update(oldDino.dino, dino)
    }
}

