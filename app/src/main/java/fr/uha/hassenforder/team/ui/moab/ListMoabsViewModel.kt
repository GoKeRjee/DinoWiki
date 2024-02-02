package fr.uha.hassenforder.team.ui.moab

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.repository.MoabRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListMoabsViewModel @Inject constructor(
    private val repository: MoabRepository
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

    fun delete(moab: Moab) = viewModelScope.launch {
        repository.delete(moab)
    }

    val moabs = repository.getAll()

}
