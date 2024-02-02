package fr.uha.hassenforder.team.ui.moab

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.FullMoab
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.repository.MoabRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MoabViewModel @Inject constructor(
    private val repository: MoabRepository
) : ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface MoabState {
        data class Success(val moab: FullMoab) : MoabState
        object Loading : MoabState
        object Error : MoabState
    }

    data class FieldWrapper<T>(
        val current: T? = null, val errorId: Int? = null
    ) {
        companion object {
            fun buildName(state: MoabUIState, newValue: String): FieldWrapper<String> {
                val errorId: Int? = MoabUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildPicture(
                state: MoabViewModel.MoabUIState, newValue: Uri?
            ): MoabViewModel.FieldWrapper<Uri?> {
                val errorId: Int? = MoabUIValidator.validatePictureChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildMembers(
                state: MoabViewModel.MoabUIState, newValue: List<Dino>?
            ): MoabViewModel.FieldWrapper<List<Dino>> {
                val errorId: Int? = MoabUIValidator.validateMembersChange(state, newValue)
                return MoabViewModel.FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri?>())
    private val _membersState = MutableStateFlow(FieldWrapper<List<Dino>>())

    private val _moabId: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialMoabState: StateFlow<MoabState> =
        _moabId.flatMapLatest { id -> repository.getMoabById(id) }.map { m ->
            if (m != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, m.moab.name))
                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, m.moab.image))
                _membersState.emit(FieldWrapper.buildMembers(uiState.value, m.members))
                MoabState.Success(moab = m)
            } else {
                MoabState.Error
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MoabState.Loading)

    private val _addMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    init {
        _addMemberId.flatMapLatest { id -> repository.getDinoById(id) }.map { p ->
            if (p != null) {
                var mm: MutableList<Dino>? =
                    _membersState.value.current?.toMutableList() ?: mutableListOf()
                mm?.add(p)
                _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
            }
        }.launchIn(viewModelScope)

        _delMemberId.map {
            var mm: MutableList<Dino> = mutableListOf()
            _membersState.value.current?.forEach { m ->
                if (m.pid != it) mm.add(m)
            }
            _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
        }.launchIn(viewModelScope)
    }

    data class MoabUIState(
        val initialState: MoabState,
        val name: FieldWrapper<String>,
        val pictureState: MoabViewModel.FieldWrapper<Uri?>,
        val members: FieldWrapper<List<Dino>>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is MoabState.Success) return null
            if (name.current != initialState.moab.moab.name) return true
            if (name.current != initialState.moab.moab.name) return true
            if (pictureState.current != initialState.moab.moab.image) return true
            if (pictureState.current != null) return true
            if (!Comparators.shallowEqualsListDinos(
                    members.current, initialState.moab.members
                )
            ) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (name.errorId != null) return true
            if (pictureState.errorId != null) return true
            if (members.errorId != null) return true
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

    val uiState: StateFlow<MoabUIState> = combine(
        _initialMoabState, _nameState, _pictureState, _membersState
    ) { initial, n, p, mm -> MoabUIState(initial, n, p, mm) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MoabUIState(
            MoabState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),

            )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class PictureChanged(val newValue: Uri?) : MoabViewModel.UIEvent()
        data class MemberAdded(val newValue: Long) : UIEvent()
        data class MemberDeleted(val newValue: Dino) : UIEvent()
    }

    data class MoabUICallback(
        val onEvent: (UIEvent) -> Unit,
    )

    val uiCallback = MoabUICallback(onEvent = {
        viewModelScope.launch {
            when (it) {
                is UIEvent.NameChanged -> _nameState.emit(
                    FieldWrapper.buildName(
                        uiState.value, it.newValue
                    )
                )

                is MoabViewModel.UIEvent.PictureChanged -> _pictureState.emit(
                    MoabViewModel.FieldWrapper.buildPicture(
                        uiState.value, it.newValue
                    )
                )

                is UIEvent.MemberAdded -> _addMemberId.emit(it.newValue)
                is UIEvent.MemberDeleted -> _delMemberId.emit(it.newValue.pid)
            }
        }
    })

    fun edit(pid: Long) = viewModelScope.launch {
        _moabId.emit(pid)
    }

    fun create(moab: Moab) = viewModelScope.launch {
        val pid: Long = repository.createMoab(moab)
        _moabId.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialMoabState.value !is MoabState.Success) return@launch
        val oldMoab = _initialMoabState.value as MoabState.Success
        val moab = FullMoab(
            Moab(
                mid = _moabId.value,
                name = _nameState.value.current!!,
                image = _pictureState.value.current
            ), members = _membersState.value.current!!
        )
        repository.saveMoab(oldMoab.moab, moab)
    }

}
