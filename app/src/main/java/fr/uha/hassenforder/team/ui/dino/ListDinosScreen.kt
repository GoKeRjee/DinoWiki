package fr.uha.hassenforder.team.ui.dino

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FavoriteBorder
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
import androidx.compose.ui.graphics.Color
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
import fr.uha.hassenforder.team.ui.theme.Purple80

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
    val genderIcon: ImageVector
    var genderTint = Color.Unspecified

    when (dino.dino.gender) {
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
            .padding(10.dp)
            .border(
            width = 1.dp,
            color = Purple80,
            shape = RoundedCornerShape(8.dp)
        ),
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
            Icon(
                imageVector = genderIcon,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
                tint = genderTint
            )
        },
    )
}