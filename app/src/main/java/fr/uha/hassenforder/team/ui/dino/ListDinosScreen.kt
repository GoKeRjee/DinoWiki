package fr.uha.hassenforder.team.ui.dino

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Adb
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.DinoWithDetails
import fr.uha.hassenforder.team.model.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDinosScreen(
    vm: ListDinosViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (p: Dino) -> Unit,
    onDelete: (p: Dino) -> Unit

) {
    val dinos = vm.dinos.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.feed() }),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = { vm.clean() })
    )


    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.dino_list)
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(
                items = dinos.value,
                key = { dino -> dino.dino.pid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item.dino) },
                    onDelete = { onDelete(item.dino) },
                ) {
                    DinoItem(item)
                }
            }
        }
    }

}

@Composable
fun DinoItem(dino: DinoWithDetails) {
    val gender: ImageVector = when (dino.dino.gender) {
        Gender.NO -> Icons.Outlined.DoNotDisturb
        Gender.MIXTE -> Icons.Outlined.Adb
    }

    ListItem(
        headlineContent = {
            Column() {
                Row(verticalAlignment = Alignment.Top) {
                    AsyncImage(
                        model = dino.dino.picture,
                        modifier = Modifier.size(96.dp),
                        contentDescription = "Selected image",
                        error = rememberVectorPainter(Icons.Outlined.Error),
                        placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column() {
                        Text(
                            "${dino.dino.type.name}, ${dino.dino.regime.name}",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                        Text(dino.dino.name, modifier = Modifier.padding(end = 4.dp))
                        Text(
                            "Apprivoisable: ${dino.dino.apprivoiser.name}",
                            modifier = Modifier.padding(end = 4.dp)
                        )
                    }
                }
            }
        },
        leadingContent = { },
        trailingContent = {
            Icon(imageVector = gender, contentDescription = null, modifier = Modifier.size(48.dp))
        },
    )
}