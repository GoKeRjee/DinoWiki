package fr.uha.hassenforder.team.ui.dino

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.repository.DinoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListDinosViewModel @Inject constructor(
    private val repository: DinoRepository
) : ViewModel() {
    fun feed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().populate()
        }
    }

    fun clean() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().clear()
        }
    }

    fun delete(dino: Dino) = viewModelScope.launch {
        repository.delete(dino)
    }

    val dinos = repository.getAll()

}
