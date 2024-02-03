package fr.uha.hassenforder.team.ui.moab

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
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Community
import fr.uha.hassenforder.team.model.Moab
import fr.uha.hassenforder.team.ui.team.UIConverter
import fr.uha.hassenforder.team.ui.theme.Purple80

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMoabsScreen(
    vm: ListMoabsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (p: Moab) -> Unit,
) {
    val moabs = vm.moabs.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.feed() }),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = { vm.clean() })
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.moab_list)
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
                items = moabs.value,
                key = { moab -> moab.mid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item) },
                    onDelete = { vm.delete(item) },
                ) {
                    moabItem(item)
                }
            }
        }
    }

}

@Composable
fun moabItem(moab: Moab) {
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
                        model = moab.image,
                        modifier = Modifier.size(96.dp),
                        contentDescription = "Selected image",
                        error = rememberVectorPainter(Icons.Outlined.Error),
                        placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            moab.name,
                            modifier = Modifier.padding(end = 4.dp),
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        )
                        Text(
                            UIConverter.convert(moab.startDay),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                        Text(
                            if (moab.community == Community.YES) "Community" else "Official",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 4.dp)
                        )
                    }
                }
            }
        },
    )
}
