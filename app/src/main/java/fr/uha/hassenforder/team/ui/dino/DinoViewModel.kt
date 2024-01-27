package fr.uha.hassenforder.team.ui.dino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.repository.DinoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class DinoViewModel(private val repository: DinoRepository) : ViewModel() {

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    fun create(dino: Dino) = viewModelScope.launch {
        val pid: Long = repository.create(dino)
        _id.emit(pid)
    }

    sealed interface DinoState {
        data class Success(val dino: Dino) : DinoState
        object Loading : DinoState
        object Error : DinoState
    }

    val dinoState: StateFlow<DinoState> = _id
        .flatMapLatest { id -> repository.getDinoById(id) }
        .map { p ->
            if (p != null) {
                DinoState.Success(p)
            } else {
                DinoState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DinoState.Loading)

}
