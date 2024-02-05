package fr.uha.hassenforder.team.ui.team

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.*
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.database.DinoDao
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class DinoPickerViewModel @Inject constructor(private val dao: DinoDao) : ViewModel() {

    val dinos: Flow<List<Dino>> = dao.getAll()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DinoPicker(
    vm: DinoPickerViewModel = hiltViewModel(),
    @StringRes title: Int?,
    onSelect: (dino: Dino?) -> Unit,
    alreadySelected: List<Dino>?
) {
    val list = vm.dinos.collectAsStateWithLifecycle(initialValue = emptyList())
    val notSelected = if (alreadySelected != null) keepNotSelectedDino(
        list.value,
        alreadySelected!!
    ) else list.value

    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(pageTitleId = title ?: R.string.dino_select) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = notSelected,
                    key = { dino -> dino.pid }
                ) { item ->
                    DinoItem(item, onSelect)
                }
            }
        }
    }
}

fun keepNotSelectedDino(value: List<Dino>, alreadySelected: List<Dino>): List<Dino> {
    return value.filter { dino -> dino !in alreadySelected }
}

@Composable
private fun DinoItem(dino: Dino, onSelect: (dino: Dino?) -> Unit) {
    val genderIcon: ImageVector
    var genderTint = Color.Unspecified

    when (dino.gender) {
        Gender.NO -> {
            genderIcon = Icons.Outlined.DoNotDisturb
            genderTint = Color.White
        }

        Gender.MIXTE -> {
            genderIcon = Icons.Outlined.FavoriteBorder
            genderTint = Color.Red
        }
    }

    ListItem(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(onClick = { onSelect(dino) }),
        headlineContent = {
            Row() {
                Text(dino.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
        trailingContent = {
            Icon(
                imageVector = genderIcon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = genderTint
            )
        }
    )
}
