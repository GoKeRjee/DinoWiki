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
) {
    val list = vm.dinos.collectAsStateWithLifecycle(initialValue = emptyList())
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
                    items = list.value,
                    key = { dino -> dino.pid }
                ) { item ->
                    DinoItem(item, onSelect)
                }
            }
        }
    }
}

@Composable
private fun DinoItem(dino: Dino, onSelect: (dino: Dino?) -> Unit) {
    val gender: ImageVector =
        when (dino.gender) {
            Gender.NO -> Icons.Outlined.DoNotDisturb
            Gender.MIXTE -> Icons.Outlined.Adb
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
        /*

                        supportingContent = {
                            Row() {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "phone", modifier = Modifier.padding(end = 8.dp))
                                Text(person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                },
                 */
        /*
                leadingContent = {
                    if (person.picture != null) {
                        AsyncImage(
                            model = person.picture,
                            modifier = Modifier.size(64.dp),
                            contentDescription = null,
                            error = rememberVectorPainter(Icons.Outlined.Error),
                            placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                        )
                    }
        },
         */
        /*
                trailingContent = {
                    Icon(imageVector = gender, contentDescription = "gender", modifier = Modifier.size(48.dp) )
                },
         */
    )
}
