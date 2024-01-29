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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Apprivoiser
import fr.uha.hassenforder.team.model.Dino
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Regime
import fr.uha.hassenforder.team.model.Type
import fr.uha.hassenforder.team.ui.theme.Team2023Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDinosScreen(
    vm: ListDinosViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (p: Dino) -> Unit,

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
                key = { dino -> dino.pid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item) },
                    onDelete = {},
                ) {
                    DinoItem(item)
                }
            }
        }
    }

}

@Composable
fun DinoItem(dino: Dino) {
    val gender: ImageVector = when (dino.gender) {
        Gender.NO -> Icons.Outlined.DoNotDisturb
        Gender.MIXTE -> Icons.Outlined.Adb
    }

    ListItem(
        headlineContent = {
            Column() { // Utilisez Column pour empiler les éléments verticalement
                Row(verticalAlignment = Alignment.Top) { // Alignez le nom avec le coin supérieur droit de l'image
                    AsyncImage(
                        model = dino.picture,
                        modifier = Modifier.size(96.dp), // Taille augmentée pour l'image
                        contentDescription = "Selected image",
                        error = rememberVectorPainter(Icons.Outlined.Error),
                        placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                    )
                    Spacer(modifier = Modifier.width(8.dp)) // Espacement entre l'image et le texte
                    Column() { // Colonne pour le texte à droite de l'image
                        Text(
                            "${dino.type.name}, ${dino.regime.name}",
                            modifier = Modifier.padding(end = 4.dp)
                        ) // Type et régime
                        Text(dino.name, modifier = Modifier.padding(end = 4.dp)) // Nom
                        Text(
                            "Apprivoisable: ${dino.apprivoiser.name}",
                            modifier = Modifier.padding(end = 4.dp)
                        ) // État d'apprivoisement
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

@Preview(showBackground = true)
@Composable
fun ListPreview() {
    Team2023Theme {
        DinoItem(
            Dino(
                0,
                "DANAZOR",
                Gender.NO,
                Type.AERIEN,
                Regime.CARNIVORE,
                Apprivoiser.INCONNU,
                null
            )
        )
    }
}