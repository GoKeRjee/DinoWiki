package fr.uha.hassenforder.team.ui.dino

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.repository.DinoRepository
import javax.inject.Inject

@HiltViewModel
class ListDinosViewModel @Inject constructor(
    private val repository: DinoRepository
) : ViewModel() {

    val dinos = repository.getAll()

}
